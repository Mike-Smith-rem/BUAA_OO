import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private MyUmlClassModelInteraction umlClassModelInteraction;
    private MyUmlStateChartInteraction umlStateChartInteraction;
    private MyUmlCollaborationInteraction umlCollaborationInteraction;

    public MyUmlGeneralInteraction(UmlElement... elements) {
        umlClassModelInteraction = new MyUmlClassModelInteraction(elements);
        umlStateChartInteraction = new MyUmlStateChartInteraction(elements);
        umlCollaborationInteraction = new MyUmlCollaborationInteraction(elements);
    }

    @Override
    public int getClassCount() {
        return umlClassModelInteraction.getClassCount();
    }

    @Override
    public int getClassOperationCount(String s) throws ClassNotFoundException,
            ClassDuplicatedException {
        return umlClassModelInteraction.getClassOperationCount(s);
    }

    @Override
    public int getClassAttributeCount(String s) throws ClassNotFoundException,
            ClassDuplicatedException {
        return umlClassModelInteraction.getClassAttributeCount(s);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModelInteraction.getClassOperationVisibility(s, s1);
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return umlClassModelInteraction.getClassAttributeVisibility(s, s1);
    }

    @Override
    public String getClassAttributeType(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException,
            AttributeWrongTypeException {
        return umlClassModelInteraction.getClassAttributeType(s, s1);
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return umlClassModelInteraction.getClassOperationParamType(s, s1);
    }

    @Override
    public List<String> getClassAssociatedClassList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return umlClassModelInteraction.getClassAssociatedClassList(s);
    }

    @Override
    public String getTopParentClass(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return umlClassModelInteraction.getTopParentClass(s);
    }

    @Override
    public List<String> getImplementInterfaceList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return umlClassModelInteraction.getImplementInterfaceList(s);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModelInteraction.getInformationNotHidden(s);
    }

    @Override
    public int getParticipantCount(String s) throws InteractionNotFoundException,
            InteractionDuplicatedException {
        return umlCollaborationInteraction.getParticipantCount(s);
    }

    @Override
    public int getIncomingMessageCount(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return umlCollaborationInteraction.getIncomingMessageCount(s, s1);
    }

    @Override
    public int getSentMessageCount(String s, String s1, MessageSort messageSort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return umlCollaborationInteraction.getSentMessageCount(s, s1, messageSort);
    }

    @Override
    public int getStateCount(String s) throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        return umlStateChartInteraction.getStateCount(s);
    }

    @Override
    public int getSubsequentStateCount(String s, String s1) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return umlStateChartInteraction.getSubsequentStateCount(s, s1);
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return umlStateChartInteraction.getTransitionTrigger(s, s1, s2);
    }
}
