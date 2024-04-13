import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class ArgsParser {

    private static final Map<String, Integer> nOfValues = Map.ofEntries(
            entry("--port", 1),
            entry("--replicaof", 2)
    );
    public static HashMap<String, String[]> parse(String[] args){
        HashMap<String,String[]> config = new HashMap<>();

        for(int i=0; i<args.length;i++){
            if (!nOfValues.containsKey(args[i]) || i+nOfValues.get(args[i])>=args.length){
                break;
            }
            int n = nOfValues.get(args[i]);
            config.put(args[i], Arrays.copyOfRange(args,i+1,i+n+1));
            i+=n;
        }

        return config;
    }
}
