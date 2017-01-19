package ca.uhn.fhir.jpa.util.jsonpatch;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.riotopsys.json_patch.JsonPath;
import net.riotopsys.json_patch.operation.AbsOperation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CopyOperation extends AbsOperation {

	public JsonPath mySourcePath;

	public CopyOperation(JsonPath theTargetPath, JsonPath theSourcePath) {
		mySourcePath = theSourcePath;
		this.path = theTargetPath;
	}

	@Override
	public String getOperationName() {
		return "copy";
	}

	@Override
	public JsonElement apply(JsonElement original) {
		JsonElement result = duplicate(original);

		JsonElement item = path.head().navigate(result);
		JsonElement data = mySourcePath.head().navigate(original);

		if (item.isJsonObject()) {
			item.getAsJsonObject().add(path.tail(), data);
		} else if (item.isJsonArray()) {

			JsonArray array = item.getAsJsonArray();

			int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

			List<JsonElement> temp = new ArrayList<JsonElement>();

			Iterator<JsonElement> iter = array.iterator();
			while (iter.hasNext()) {
				JsonElement stuff = iter.next();
				iter.remove();
				temp.add(stuff);
			}

			temp.add(index, data);

			for (JsonElement stuff : temp) {
				array.add(stuff);
			}

		}

		return result;
	}

}
