import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private Map<String, String> id2machine = new HashMap<>();
    private Map<String, String> id2State = new HashMap<>();
    private Map<String, String> stateId2machine = new HashMap<>();
    private Map<String, Map<String, ArrayList<String>>> machine2state2id = new HashMap<>();
    private Map<String, ArrayList<String>> machine2id = new HashMap<>();
    private Map<String, Integer> machine2stateCount = new HashMap<>();
    private Map<String, ArrayList<String>> stateId2Event = new HashMap<>();
    private Map<String, String> event2StateId = new HashMap<>();
    private Map<String, Map<String, String>> transitId2FromTo = new HashMap<>();
    private Map<String, ArrayList<String>> transitId2event = new HashMap<>();
    private Map<String, Map<String, ArrayList<String>>> machine2stateNextTo = new HashMap<>();
    private Map<String, Map<String, Integer>> machine2stateNextCount = new HashMap<>();
    private Map<String, Map<String, Integer>> machine2stateFlag = new HashMap<>();
    private Map<String, String> sta = new HashMap<>();

    public MyUmlStateChartInteraction(UmlElement... elements) {
        ArrayList<UmlElement> level2 = new ArrayList<>();
        ArrayList<UmlElement> level3 = new ArrayList<>();
        ArrayList<UmlElement> level4 = new ArrayList<>();
        ArrayList<UmlElement> level5 = new ArrayList<>();
        for (UmlElement e : elements) {
            String id = e.getId();
            String parent = e.getParentId();
            String name = e.getName();
            switch (e.getElementType()) {
                case UML_STATE_MACHINE:
                    id2machine.put(id, name);
                    break;
                case UML_REGION:
                    level2.add(e);
                    break;
                case UML_STATE:
                case UML_PSEUDOSTATE:
                case UML_FINAL_STATE:
                    level3.add(e);
                    break;
                case UML_TRANSITION:
                    level4.add(e);
                    break;
                case UML_EVENT:
                    level5.add(e);
                    break;
                case UML_OPAQUE_BEHAVIOR:
                default:
                    break;
            }
        }
        for (UmlElement e : level2) {
            //UML_REGION
            String id = e.getId();
            String parent = e.getParentId();
            String name = id2machine.get(parent);
            id2machine.remove(parent, name);
            id2machine.put(id, name);
            if (!machine2id.containsKey(name)) {
                machine2id.put(name, new ArrayList<>());
            }
            machine2id.get(name).add(id);
            machine2stateCount.put(id, 0);
            machine2state2id.put(id, new HashMap<>());
            machine2stateFlag.put(id, new HashMap<>());
            machine2stateNextTo.put(id, new HashMap<>());
            machine2stateNextCount.put(id, new HashMap<>());
        }
        for (UmlElement e : level3) {
            //UML_STATE
            String id = e.getId();
            String parent = e.getParentId();
            String name = e.getName();
            id2State.put(id, name);
            stateId2Event.put(id, new ArrayList<>());
            stateId2machine.put(id, parent);
            if (name != null) {
                if (!machine2state2id.get(parent).containsKey(name)) {
                    machine2state2id.get(parent).put(name, new ArrayList<>());
                }
                machine2state2id.get(parent).get(name).add(id);
                machine2stateNextTo.get(parent).put(name, new ArrayList<>());
                machine2stateNextCount.get(parent).put(name, 0);
            }
            machine2stateCount.put(parent, machine2stateCount.get(parent) + 1);
        }
        for (UmlElement e : level4) {
            //UML_TRANSIT
            String id = e.getId();
            String parent = e.getParentId();
            String name = e.getName();
            UmlTransition e1 = (UmlTransition) e;
            String source = e1.getSource();
            String target = e1.getTarget();
            transitId2event.put(id, new ArrayList<>());
            Map<String, String> t = new HashMap<>();
            t.put(source, target);
            transitId2FromTo.put(id, t);
            if (machine2stateNextTo.get(stateId2machine.get(source)).
                    containsKey(id2State.get(source))
                    && !machine2stateNextTo.get(stateId2machine.get(source)).
                    get(id2State.get(source)).
                    contains(target)) {
                machine2stateNextTo.get(stateId2machine.get(source))
                        .get(id2State.get(source)).add(target);
                machine2stateNextCount.get(stateId2machine.get(source))
                        .put(id2State.get(source), machine2stateNextCount.
                                get(stateId2machine.get(source)).get(id2State.get(source)) + 1);
            }
        }
        for (UmlElement e : level5) {
            //UML_EVENT
            String id = e.getId();
            String parent = e.getParentId();
            String name = e.getName();
            if (e instanceof UmlEvent) {
                UmlEvent e1 = (UmlEvent) e;
                transitId2event.get(parent).add(name);
                Map<String, String> t = transitId2FromTo.get(parent);
                for (String from : t.keySet()) {
                    String to = t.get(from);
                    if (!stateId2Event.containsKey(from)) {
                        stateId2Event.put(from, new ArrayList<>());
                    }
                    stateId2Event.get(from).add(name);
                    event2StateId.put(name, to);
                }
            }
        }
        init();
    }

    private void init() {
        init2();
        init2();
    }

    private void init2() {
        for (String machine : machine2stateNextTo.keySet()) {
            Map<String, ArrayList<String>> states = machine2stateNextTo.get(machine);
            sta.clear();
            for (String i : states.keySet()) {
                dfs(i, machine);
                sta.clear();
            }
            machine2stateFlag.get(machine).clear();
        }
    }

    private void dfs(String state, String machine) {
        if (machine2stateFlag.get(machine).containsKey(state)
                || !machine2stateNextCount.get(machine).containsKey(state)
                || machine2stateNextCount.get(machine).get(state) == 0
                || sta.containsKey(state)) {
            return;
        }
        sta.put(state, state);
        ArrayList<String> tmp = new ArrayList<>();
        for (String e1 : machine2stateNextTo.get(machine).get(state)) {
            String e = id2State.get(e1);
            dfs(e, machine);
            ArrayList<String> father = machine2stateNextTo.get(machine).get(e);
            if (father != null) {
                tmp.addAll(father);
            }
        }
        for (String e : tmp) {
            if (!machine2stateNextTo.get(machine).get(state).contains(e)) {
                machine2stateNextTo.get(machine).get(state).add(e);
                machine2stateNextCount.get(machine).put(state,
                        machine2stateNextCount.get(machine).get(state) + 1);
            }
        }
        machine2stateFlag.get(machine).put(state, 1);
    }

    @Override
    public int getStateCount(String s) throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        if (!machine2id.containsKey(s)) {
            throw new StateMachineNotFoundException(s);
        }
        if (machine2id.get(s).size() != 1) {
            throw new StateMachineDuplicatedException(s);
        }
        String id = machine2id.get(s).get(0);
        return machine2stateCount.get(id);
    }

    @Override
    public int getSubsequentStateCount(String s, String s1) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        exception(s, s1);
        String id = machine2id.get(s).get(0);
        return machine2stateNextCount.get(id).get(s1);
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException, StateNotFoundException,
            StateDuplicatedException, TransitionNotFoundException {
        exception(s, s1);
        exception(s, s2);
        String id = machine2id.get(s).get(0);
        String stateId1 = machine2state2id.get(id).get(s1).get(0);
        String stateId2 = machine2state2id.get(id).get(s2).get(0);
        ArrayList<String> ans = new ArrayList<>();
        if (!stateId2Event.containsKey(stateId1)) {
            return ans;
        }
        for (String t : stateId2Event.get(stateId1)) {
            if (event2StateId.get(t).equals(stateId2)) {
                ans.add(t);
            }
        }
        if (ans.size() == 0) {
            throw new TransitionNotFoundException(s, s1, s2);
        }
        return ans;
    }

    private void exception(String s, String s1) throws StateMachineNotFoundException,
            StateMachineDuplicatedException, StateNotFoundException,
            StateDuplicatedException {
        if (!machine2id.containsKey(s)) {
            throw new StateMachineNotFoundException(s);
        }
        if (machine2id.get(s).size() != 1) {
            throw new StateMachineDuplicatedException(s);
        }
        String id = machine2id.get(s).get(0);
        Map<String, ArrayList<String>> states = machine2state2id.get(id);
        if (!states.containsKey(s1)) {
            throw new StateNotFoundException(s, s1);
        }
        if (states.get(s1).size() != 1) {
            throw new StateDuplicatedException(s, s1);
        }
    }
}
