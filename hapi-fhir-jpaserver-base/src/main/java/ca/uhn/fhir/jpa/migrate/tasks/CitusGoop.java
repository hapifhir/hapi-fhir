package ca.uhn.fhir.jpa.migrate.tasks;

import com.google.common.base.Strings;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class CitusGoop {

	Stream<String> getFileStatements() {
		Resource resource = new ClassRelativeResourceLoader(CitusGoop.class).getResource("convert-schema-to-citus.sql");
		try {
			return new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines();
		} catch (IOException theE) {
			throw new RuntimeException(theE);
		}
	}

	static Stream<String> addPartitionToPK(String theTable) {
		var pkName = theTable + "_pkey";
		return Stream.of(
			"alter table " + theTable + " drop constraint " + pkName,
			"alter table " + theTable + " alter column partition_id set not null",
			"alter table " + theTable + " add constraint " + pkName + " PRIMARY KEY (sp_id, partition_id)",
			"DO $$ BEGIN PERFORM create_distributed_table('" + theTable + "', 'partition_id'); END; $$"
		);
	}

	@Nonnull
	Stream<String> getSqlStatements() {
		return getFileStatements()
			.map(String::trim)
			.filter(s->!s.isBlank())
			.filter(s->!s.startsWith("--")) // skip comments
			;
//		return Stream.concat(
//			Stream.of("DO $$BEGIN PERFORM create_reference_table('hfj_resource'); END;$$;"),
//			Stream.of(
//				"hfj_spidx_coords",
//				"hfj_spidx_date",
//				"hfj_spidx_number",
//				"hfj_spidx_string",
//				"hfj_spidx_token",
//				"hfj_spidx_uri",
//				"hfj_spidx_quantity",
//				"hfj_spidx_quantity_nrml"
//			).flatMap(CitusGoop::addPartitionToPK)
//
//		).map(String::trim).map(s->s.endsWith(";")?s:s+";");
	}

	public static void main(String[] args) {
		new CitusGoop().getSqlStatements().forEach(System.out::println);
	}
}
