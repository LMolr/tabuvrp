package tabuvrp.tabustage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;


public class TabuIndex<S, T> {

    protected final HashMap<T, HashSet<S>> tabu;
    protected final HashMap<T, HashSet<Integer>> backptrs;
    protected final ArrayList<HashMap<T, HashSet<S>>> expirations;
    protected final int maxExpTime;
    protected int now;


    public TabuIndex(int maxExpirationTime) {
        if (maxExpirationTime < 0) {
            throw new IllegalArgumentException("cannot instantiate a tabu index with maximum expiration time < 0");
        }
        tabu = new HashMap<T, HashSet<S>>();
        backptrs = new HashMap<T, HashSet<Integer>>();
        expirations = new ArrayList<HashMap<T, HashSet<S>>>(maxExpirationTime + 1);
        for (int i = 0; i < maxExpirationTime + 1; ++i) {
            expirations.add(new HashMap<T, HashSet<S>>());
        }
        maxExpTime = maxExpirationTime;
        now = 0;
    }

    public void setTabu(S source, T target, int expireAfter) {
        /* Pre-checks */
        if (expireAfter < 0) {
            return;
        }
        if (expireAfter > maxExpTime) {
            throw new IllegalArgumentException("expiration time not in [0.." + maxExpTime + "]");
        }
        if (   source == null
            || target == null ) {
            throw new IllegalArgumentException("cannot set null values as tabu");
        }

        /* Mark tabu */
        HashSet<S> sources = tabu.get(target);
        if (sources == null) {
            // 'target' doesn't have a tabu set: make it
            sources = new HashSet<S>();
            tabu.put(target, sources);
        }
        else {
            if (sources.contains(source)) {
                // 'source' is already tabu for 'target': return
                return;
            }
        }
        // add 'source' as tabu for 'target'
        sources.add(source);

        /* Schedule expiration */
        Integer expTime = (now + expireAfter + 1) % (maxExpTime + 1);
        HashMap<T, HashSet<S>> expMap = expirations.get(expTime);
        HashSet<S> expSet = expMap.get(target);
        if (expSet == null) {
            expSet = new HashSet<S>();
            expMap.put(target, expSet);
        }
        expSet.add(source);

        /* Add backpointer */
        HashSet<Integer> expTimes = backptrs.get(target);
        if (expTimes == null) {
            expTimes = new HashSet<Integer>();
            backptrs.put(target, expTimes);
        }
        expTimes.add(expTime);
    }

    public void clearTarget(T target) {
        /* Pre-checks */
        if (target == null) {
            return;
        }
        HashSet<S> sources = tabu.get(target);
        if (sources == null) {
            return;
        }

        /* Remove tabu entries for 'target' */
        tabu.remove(target);


        /* Remove scheduled expirations for 'target' */
        for (Integer time : backptrs.get(target)) {
            expirations.get(time).remove(target);
        }

        /* Remove backpointers for 'target' */
        backptrs.remove(target);
    }

    public boolean isTabu(S source, T target) {
        if (   source == null
            || target == null ) {
            return false;
        }
        HashSet<S> sources = tabu.get(target);
        return (sources != null &&
                sources.contains(source) );
    }

    public void step() {
        now = (now + 1) % (maxExpTime + 1);
        HashMap<T, HashSet<S>> expMap = expirations.get(now);
        
        for (T target : expMap.keySet()) {

            HashSet<S> expSources = expMap.get(target);
            tabu.get(target).removeAll(expSources);
            if (expSources.isEmpty()) {
                tabu.remove(target);
            }

            HashSet<Integer> targetBackptrs = backptrs.get(target);
            targetBackptrs.remove(now);
            if (targetBackptrs.isEmpty()) {
                backptrs.remove(target);
            }
        }

        expMap.clear();
    }

}
