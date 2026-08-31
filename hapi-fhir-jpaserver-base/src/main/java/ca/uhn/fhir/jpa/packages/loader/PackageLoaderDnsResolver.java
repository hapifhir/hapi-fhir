/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.i18n.Msg;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.hl7.fhir.utilities.http.ManagedWebAccessUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Stream;

public class PackageLoaderDnsResolver implements DnsResolver {

	static class BlackListEntry {

		private final String myEntry;
		private final byte[] myNetwork;
		private final int myPrefixLength;

		BlackListEntry(String theEntry, byte[] theNetwork, int thePrefixLength) {
			myEntry = theEntry;
			myNetwork = theNetwork;
			myPrefixLength = thePrefixLength;
		}

		public static BlackListEntry parse(String theEntry) {
			String[] parts = theEntry.split("/", 2);
			if (!InetAddressUtils.isIPv4Address(parts[0]) && !InetAddressUtils.isIPv6Address(parts[0])) {
				throw new IllegalArgumentException(Msg.code(3038) + "Not a valid ip address: " + theEntry);
			}

			byte[] network;
			try {
				network = InetAddress.getByName(parts[0]).getAddress();
			} catch (UnknownHostException ex) {
				throw new IllegalArgumentException(Msg.code(3039) + "Unparseable Blacklist entry: " + theEntry, ex);
			}

			int addressBits = network.length * 8;
			int prefixLength = parts.length == 1 ? addressBits : Integer.parseInt(parts[1]);
			if (prefixLength < 0 || prefixLength > addressBits) {
				throw new IllegalArgumentException(
						Msg.code(3040) + "Prefix length out of range for block list entry: " + theEntry);
			}

			BlackListEntry retval = new BlackListEntry(theEntry, network, prefixLength);

			return retval;
		}

		public boolean contains(InetAddress theAddress) {
			byte[] candidate = theAddress.getAddress();
			if (candidate.length != myNetwork.length) {
				// not the same address (IPv4 vs IPv6, say)
				return false;
			}
			int fullBytes = myPrefixLength / 8;
			int remainingBits = myPrefixLength % 8;
			for (int i = 0; i < fullBytes; i++) {
				if (candidate[i] != myNetwork[i]) {
					return false;
				}
			}
			if (remainingBits == 0) {
				return true;
			}

			/*
			 * we need to check the subnet mask
			 * 0xFF   = 1111 1111
			 * << 8 - N
			 *        = 1111 1111 (8-N)0s
			 * & 0xFF
			 *        = N1s (8-N)0s
			 *
			 * eg: N = 5
			 * 0xFF = 1111 1111
			 * << 3 (8 - 5)
			 *      = 0111 1111 1000
			 * & 0xFF
			 *      = 1111 1000
			 *
			 * then use this mask to check range in network
			 */
			int mask = (0xFF << (8 - remainingBits)) & 0xFF;
			return (candidate[fullBytes] & mask) == (myNetwork[fullBytes] & mask);
		}

		@Override
		public String toString() {
			return myEntry;
		}
	}

	private static final List<BlackListEntry> BLACK_LIST = Stream.of(
					"169.254.169.254", // AWS, Azure, and GCP metadata
					"100.64.0.0/10", // carrier-grade NAT
					"192.0.0.192" // azure/oracle cloud metadata
					)
			.map(BlackListEntry::parse)
			.toList();

	private final PackageUrlAllowList myPackageUrlAllowList;

	private final DnsResolver myDnsResolver;

	public PackageLoaderDnsResolver(PackageUrlAllowList theAllowList) {
		this(theAllowList, SystemDefaultDnsResolver.INSTANCE);
	}

	/**
	 * Visible for testing; so we can map our own hostname onto an arbitrary address
	 */
	PackageLoaderDnsResolver(PackageUrlAllowList theAllowList, DnsResolver theDnsResolver) {
		myPackageUrlAllowList = theAllowList;
		myDnsResolver = theDnsResolver;
	}

	@Override
	public InetAddress[] resolve(String theHost) throws UnknownHostException {
		boolean privateAllowed = myPackageUrlAllowList.isPrivateNetworkAllowedForHost(theHost);

		InetAddress[] addresses = myDnsResolver.resolve(theHost);

		for (InetAddress address : addresses) {
			BlackListEntry entry = BLACK_LIST.stream()
					.filter(network -> network.contains(address))
					.findFirst()
					.orElse(null);
			if (entry != null) {
				throw new UnknownHostException(Msg.code(3041) + "Refusing to connect to " + theHost
						+ "; resolved address " + address.getHostAddress() + " is blocked by " + entry);
			}

			if (!privateAllowed) {
				try {
					ManagedWebAccessUtils.throwExceptionIfNonPublicAddress(address, theHost);
				} catch (IOException ex) {
					UnknownHostException toThrow = new UnknownHostException(Msg.code(3042) + "Host " + theHost
							+ " resolves to a non-public address " + address.getHostAddress());
					toThrow.initCause(ex);
					throw toThrow;
				}
			}
		}

		return addresses;
	}
}
