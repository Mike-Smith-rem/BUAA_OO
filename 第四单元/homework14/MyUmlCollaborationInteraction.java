import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private Map<String, String> id2col = new HashMap<>();//id-----collaborate
    private Map<String, String> id2attr = new HashMap<>();//id----attribute
    private Map<String, String> id2endPoint = new HashMap<>();//id----endpoint
    private Map<String, ArrayList<String>> col2id = new HashMap<>();//
    private Map<String, Map<String/*name*/,
            ArrayList<String>>> col2para = new HashMap<>();
    private Map<String/*id*/, Integer> col2num = new HashMap<>();
    private Map<String, Map<String/*name*/,Integer>> col2para2In = new HashMap<>();
    private Map<String, Map<String/*name*/,
            Map<MessageSort, Integer>>> col2para2Out = new HashMap<>();
    private Map<String, String> para2col = new HashMap<>();

    public MyUmlCollaborationInteraction(UmlElement... elements) {
        ArrayList<UmlElement> level2 = new ArrayList<>();
        ArrayList<UmlElement> level3 = new ArrayList<>();
        ArrayList<UmlElement> level4 = new ArrayList<>();
        for (UmlElement e : elements) {
            String id = e.getId();
            String name = e.getName();
            switch (e.getElementType()) {
                case UML_COLLABORATION:
                    id2col.put(id, name);
                    break;
                case UML_INTERACTION:
                    level2.add(e);
                    break;
                case UML_LIFELINE:
                    level3.add(e);
                    break;
                case UML_ENDPOINT:
                    id2endPoint.put(id, name);
                    break;
                case UML_MESSAGE:
                    level4.add(e);
                    break;
                default:
                    break;
            }
        }
        for (UmlElement e : level2) {
            String id = e.getId();
            String parent = e.getParentId();
            String name = id2col.get(parent) == null ?
                    e.getName() : id2col.get(parent);
            id2col.put(id, name);
            id2col.remove(parent, name);
            if (!col2id.containsKey(name)) {
                col2id.put(name, new ArrayList<>());
            }
            col2id.get(name).add(id);
            col2para2In.put(id, new HashMap<>());
            col2para2Out.put(id, new HashMap<>());
            col2para.put(id, new HashMap<>());
            col2num.put(id, 0);
        }
        for (UmlElement e : level3) {
            String id = e.getId();
            String parent = e.getParentId();
            String name = e.getName();
            id2attr.put(id, name);
            col2num.put(parent, col2num.get(parent) + 1);
            if (!col2para.get(parent).containsKey(name)) {
                col2para.get(parent).put(name, new ArrayList<>());
            }
            col2para.get(parent).get(name).add(id);
            col2para2In.get(parent).put(name, 0);
            col2para2Out.get(parent).put(name, new HashMap<>());
            para2col.put(id, parent);
        }
        for (UmlElement e : level4) {
            UmlMessage e1 = (UmlMessage) e;
            String source = e1.getSource();
            String target = e1.getTarget();
            MessageSort sort = e1.getMessageSort();
            if (!id2endPoint.containsKey(source)) {
                Map<MessageSort, Integer> t1 = col2para2Out.get(para2col.get(source)).
                        get(id2attr.get(source));
                if (t1.containsKey(sort)) {
                    t1.put(sort, t1.get(sort) + 1);
                } else {
                    t1.put(sort, 1);
                }
            }
            if (!id2endPoint.containsKey(target)) {
                col2para2In.get(para2col.get(target)).put(id2attr.get(target),
                        col2para2In.get(para2col.get(target)).get(id2attr.get(target)) + 1);
            }
        }
    }

    @Override
    public int getParticipantCount(String s) throws InteractionNotFoundException,
            InteractionDuplicatedException {
        exception(s);
        String id = col2id.get(s).get(0);
        return col2num.get(id);
    }

    @Override
    public int getIncomingMessageCount(String s, String s1) throws InteractionNotFoundException,
            InteractionDuplicatedException, LifelineNotFoundException, LifelineDuplicatedException {
        exception(s);
        String id = col2id.get(s).get(0);
        if (!col2para.get(id).containsKey(s1)) {
            throw new LifelineNotFoundException(s, s1);
        }
        if (col2para.get(id).get(s1).size() != 1) {
            throw new LifelineDuplicatedException(s, s1);
        }
        return col2para2In.get(id).get(s1);
    }

    @Override
    public int getSentMessageCount(String s, String s1, MessageSort messageSort)
            throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException,
            LifelineDuplicatedException {
        exception(s);
        String id = col2id.get(s).get(0);
        if (!col2para.get(id).containsKey(s1)) {
            throw new LifelineNotFoundException(s, s1);
        }
        if (col2para.get(id).get(s1).size() != 1) {
            throw new LifelineDuplicatedException(s, s1);
        }
        return col2para2Out.get(id).get(s1).get(messageSort) == null ? 0 :
                col2para2Out.get(id).get(s1).get(messageSort);
    }

    private void exception(String s) throws InteractionNotFoundException,
            InteractionDuplicatedException {
        if (!col2id.containsKey(s)) {
            throw new InteractionNotFoundException(s);
        }
        if (col2id.get(s).size() != 1) {
            throw new InteractionDuplicatedException(s);
        }
    }
}
