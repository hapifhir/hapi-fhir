package ca.uhn.fhir.jpa.util.jsonpatch;

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
