import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.format.UmlClassModelInteraction;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.NamedType;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oocourse.uml2.models.common.ElementType.UML_CLASS;
import static com.oocourse.uml2.models.common.ElementType.UML_INTERFACE;

public class MyUmlClassModelInteraction implements UmlClassModelInteraction {
    private Map<String, String> id2name = new HashMap<>();
    private Map<String, String> id2TopClass = new HashMap<>();
    private Map<String, ArrayList<String>> name2id = new HashMap<>();
    private Map<String, Map<String, ArrayList<String>>> id2opt2id = new HashMap<>();
    private Map<String, ElementType> id2Type = new HashMap<>();
    private Map<String, ArrayList<Map<NameableType, Direction>>> optid2para = new HashMap<>();
    private int classCount = 0;
    private Map<String, Integer> idOptNum = new HashMap<>();
    private Map<String, Integer> idAttrNum = new HashMap<>();
    private Map<String, Map<String, ArrayList<Visibility>>> idOptVis = new HashMap<>();
    private Map<String, Map<String, Map<String, ArrayList<Visibility>>>> idAttrVis =
            new HashMap<>();
    private Map<String, Map<String, ArrayList<NameableType>>> idAttrType = new HashMap<>();
    private Map<String, ArrayList<String>> idExtendTo = new HashMap<>();
    private Map<String, ArrayList<String>> idAssociateTo = new HashMap<>();
    private Map<String, ArrayList<String>> idImplementTo = new HashMap<>();
    private Map<String, Integer> idAttrNumFlag = new HashMap<>();
    private Map<String, Integer> idImplementFlag = new HashMap<>();

    public MyUmlClassModelInteraction(UmlElement... elements) {
        ArrayList<UmlElement> level1 = new ArrayList<>();
        ArrayList<UmlElement> level2 = new ArrayList<>();
        ArrayList<UmlElement> level3 = new ArrayList<>();
        Map<String, ArrayList<String>> end2end = new HashMap<>();
        Map<String, String> end2Class = new HashMap<>();
        for (UmlElement e : elements) {
            String id = e.getId();
            String name = e.getName();
            String parent = e.getParentId();
            switch (e.getElementType()) {
                case UML_CLASS:
                    id2name.put(id, name);
                    if (!name2id.containsKey(name)) {
                        name2id.put(name, new ArrayList<>());
                    }
                    name2id.get(name).add(id);
                    id2opt2id.put(id, new HashMap<>());
                    id2TopClass.put(id, id);
                    id2Type.put(id, UML_CLASS);
                    classCount++;
                    idOptNum.put(id, 0);
                    idOptVis.put(id, new HashMap<>());
                    idAttrNum.put(id, 0);
                    idAttrType.put(id, new HashMap<>());
                    idAttrVis.put(id, new HashMap<>());
                    break;
                case UML_INTERFACE:
                    id2name.put(id, name);
                    if (!name2id.containsKey(name)) {
                        name2id.put(name, new ArrayList<>());
                    }
                    name2id.get(name).add(id);
                    id2opt2id.put(id, new HashMap<>());
                    id2Type.put(id, UML_INTERFACE);
                    idOptNum.put(id, 0);
                    idOptVis.put(id, new HashMap<>());
                    break;
                default:
                    level1.add(e);
                    break;
            }
        }
        for (UmlElement e : level1) {
            switch (e.getElementType()) {
                case UML_GENERALIZATION:
                    String source = ((UmlGeneralization) e).getSource();
                    String target = ((UmlGeneralization) e).getTarget();
                    if (id2Type.get(target).equals(UML_CLASS)) {
                        if (!idExtendTo.containsKey(source)) {
                            idExtendTo.put(source, new ArrayList<>());
                        }
                        idExtendTo.get(source).add(target);
                    } else {
                        if (!idImplementTo.containsKey(source)) {
                            idImplementTo.put(source, new ArrayList<>());
                        }
                        idImplementTo.get(source).add(target);
                    }
                    break;
                case UML_INTERFACE_REALIZATION:
                    source = ((UmlInterfaceRealization) e).getSource();
                    target = ((UmlInterfaceRealization) e).getTarget();
                    if (!idImplementTo.containsKey(source)) {
                        idImplementTo.put(source, new ArrayList<>());
                    }
                    idImplementTo.get(source).add(target);
                    break;
                default:
                    level2.add(e);
                    break;
            }
        }
        for (UmlElement e : level2) {
            String id = e.getId();
            String name = e.getName();
            String parent = e.getParentId();
            switch (e.getElementType()) {
                case UML_ASSOCIATION:
                    String end1 = ((UmlAssociation) e).getEnd1();
                    String end2 = ((UmlAssociation) e).getEnd2();
                    if (!end2end.containsKey(end1)) {
                        end2end.put(end1, new ArrayList<>());
                    }
                    if (!end2end.containsKey(end2)) {
                        end2end.put(end2, new ArrayList<>());
                    }
                    end2end.get(end1).add(end2);
                    end2end.get(end2).add(end1);
                    break;
                case UML_ATTRIBUTE:
                    Visibility v = ((UmlAttribute) e).getVisibility();
                    NameableType n = ((UmlAttribute) e).getType();
                    if (id2name.containsKey(parent)) {
                        if (!idAttrVis.get(parent).containsKey(name)) {
                            idAttrVis.get(parent).put(name, new HashMap<>());
                        }
                        if (!idAttrVis.get(parent).get(name).containsKey(parent)) {
                            idAttrVis.get(parent).get(name).put(parent, new ArrayList<>());
                        }
                        idAttrVis.get(parent).get(name).get(parent).add(v);
                        if (!idAttrType.get(parent).containsKey(name)) {
                            idAttrType.get(parent).put(name, new ArrayList<>());
                        }
                        idAttrType.get(parent).get(name).add(n);
                        idAttrNum.put(parent, idAttrNum.get(parent) + 1);
                    }
                    break;
                case UML_OPERATION:
                    v = ((UmlOperation) e).getVisibility();
                    if (id2name.containsKey(parent)) {
                        if (!idOptVis.get(parent).containsKey(name)) {
                            idOptVis.get(parent).put(name, new ArrayList<>());
                        }
                        idOptVis.get(parent).get(name).add(v);
                        idOptNum.put(parent, idOptNum.get(parent) + 1);
                    }
                    if (!optid2para.containsKey(id)) {
                        optid2para.put(id, new ArrayList<>());
                    }
                    if (id2opt2id.containsKey(parent)) {
                        Map<String, ArrayList<String>> t = id2opt2id.get(parent);
                        if (!t.containsKey(name)) {
                            t.put(name, new ArrayList<>());
                        }
                        t.get(name).add(id);
                    }
                    break;
                default:
                    level3.add(e);
                    break;
            }
        }
        for (UmlElement e : level3) {
            String id = e.getId();
            String parent = e.getParentId();
            switch (e.getElementType()) {
                case UML_PARAMETER:
                    Direction d = ((UmlParameter) e).getDirection();
                    NameableType n = ((UmlParameter) e).getType();
                    if (!optid2para.containsKey(parent)) {
                        optid2para.put(parent, new ArrayList<>());
                    }
                    Map<NameableType, Direction> i = new HashMap<>();
                    i.put(n, d);
                    optid2para.get(parent).add(i);
                    break;
                case UML_ASSOCIATION_END:
                    String refer = ((UmlAssociationEnd) e).getReference();
                    if (!end2Class.containsKey(id)) {
                        end2Class.put(id, refer);
                    }
                    break;
                default:
                    break;
            }
        }
        for (String t : end2end.keySet()) {
            ArrayList<String> string = end2end.get(t);
            String refer = end2Class.get(t);
            for (String s : string) {
                if (end2Class.containsKey(s)) {
                    String refer2 = end2Class.get(s);
                    if (id2Type.get(refer).equals(UML_CLASS)
                            && id2Type.get(refer).equals(UML_CLASS)) {
                        if (!idAssociateTo.containsKey(refer2)) {
                            idAssociateTo.put(refer2, new ArrayList<>());
                        }
                        if (!idAssociateTo.containsKey(refer)) {
                            idAssociateTo.put(refer, new ArrayList<>());
                        }
                        if (!idAssociateTo.get(refer).contains(refer2)) {
                            idAssociateTo.get(refer).add(refer2);
                        }
                        if (!idAssociateTo.get(refer2).contains(refer)) {
                            idAssociateTo.get(refer2).add(refer);
                        }
                    }
                }
            }
        }
        init();
    }

    private void init() {
        for (String id : idExtendTo.keySet()) {
            dfs(id);
        }
        for (String id : idImplementTo.keySet()) {
            implementDfs(id);
        }
    }

    private void implementDfs(String id) {
        if (!idImplementTo.containsKey(id)
                || idImplementFlag.containsKey(id)) {
            return;
        }
        ArrayList<String> tmp = new ArrayList<>();
        for (String e : idImplementTo.get(id)) {
            implementDfs(e);
            ArrayList<String> father = idImplementTo.get(e);
            if (father != null) {
                tmp.addAll(father);
            }
        }
        for (String e : tmp) {
            if (!idImplementTo.get(id).contains(e)) {
                idImplementTo.get(id).add(e);
            }
        }
        idImplementFlag.put(id, 1);
    }

    private void dfs(String id) {
        if (!idExtendTo.containsKey(id)
                || idExtendTo.get(id).contains(id)
                || idAttrNumFlag.containsKey(id)) {
            return;
        }
        String e = idExtendTo.get(id).get(0);
        dfs(e);
        idAttrNum.put(id, idAttrNum.get(id) + idAttrNum.get(e));
        id2TopClass.put(id, id2TopClass.get(e));
        Map<String, Map<String, ArrayList<Visibility>>> son = idAttrVis.get(id);
        Map<String, Map<String, ArrayList<Visibility>>> father = idAttrVis.get(e);
        if (!idAssociateTo.containsKey(e)) {
            idAssociateTo.put(e, new ArrayList<>());
        }
        if (!idAssociateTo.containsKey(id)) {
            idAssociateTo.put(id, new ArrayList<>());
        }
        ArrayList<String> fatherAssociate = idAssociateTo.get(e);
        ArrayList<String> sonAssociate = idAssociateTo.get(id);
        for (String idfather : father.keySet()) {
            if (son.containsKey(idfather)) {
                son.get(idfather).putAll(father.get(idfather));
            } else {
                son.put(idfather, father.get(idfather));
            }
        }
        Map<String, ArrayList<NameableType>> son1 = idAttrType.get(id);
        Map<String, ArrayList<NameableType>> father1 = idAttrType.get(e);
        for (String idfather : father1.keySet()) {
            if (son1.containsKey(idfather)) {
                son1.get(idfather).addAll(father1.get(idfather));
            } else {
                son1.put(idfather, father1.get(idfather));
            }
        }
        for (String associate : fatherAssociate) {
            if (!sonAssociate.contains(associate)
                    && id2Type.get(associate).equals(UML_CLASS)) {
                sonAssociate.add(associate);
            }
        }
        if (idImplementTo.containsKey(e)) {
            if (!idImplementTo.containsKey(id)) {
                idImplementTo.put(id, new ArrayList<>());
            }
            ArrayList<String> fatherImplement = idImplementTo.get(e);
            ArrayList<String> sonImplement = idImplementTo.get(id);
            for (String idfather : fatherImplement) {
                if (!sonImplement.contains(idfather)) {
                    sonImplement.add(idfather);
                }
            }
        }
        idAttrNumFlag.put(id, 1);
    }

    @Override
    public int getClassCount() {
        return classCount;
    }

    @Override
    public int getClassOperationCount(String className) throws ClassNotFoundException,
            ClassDuplicatedException {
        exceptionClass(className);
        String id = name2id.get(className).get(0);
        return idOptNum.get(id);
    }

    @Override
    public int getClassAttributeCount(String className) throws ClassNotFoundException,
            ClassDuplicatedException {
        exceptionClass(className);
        String id = name2id.get(className).get(0);
        return idAttrNum.get(id);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        exceptionClass(className);
        Map<Visibility, Integer> ans = new HashMap<>();
        ans.put(Visibility.PUBLIC, 0);
        ans.put(Visibility.PROTECTED, 0);
        ans.put(Visibility.PACKAGE, 0);
        ans.put(Visibility.PRIVATE, 0);
        String id = name2id.get(className).get(0);
        if (!idOptVis.get(id).containsKey(operationName)) {
            return ans;
        }
        ArrayList<Visibility> i = idOptVis.get(id).get(operationName);
        for (Visibility j : i) {
            switch (j) {
                case PUBLIC:
                    ans.put(Visibility.PUBLIC, ans.get(Visibility.PUBLIC) + 1);
                    break;
                case PRIVATE:
                    ans.put(Visibility.PRIVATE, ans.get(Visibility.PRIVATE) + 1);
                    break;
                case PROTECTED:
                    ans.put(Visibility.PROTECTED, ans.get(Visibility.PROTECTED) + 1);
                    break;
                case PACKAGE:
                    ans.put(Visibility.PACKAGE, ans.get(Visibility.PACKAGE) + 1);
                    break;
                default:
                    break;
            }
        }
        return ans;
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        exceptionClass(className);
        exceptionAttr(className, attributeName);
        String id = name2id.get(className).get(0);
        for (String i : idAttrVis.get(id).get(attributeName).keySet()) {
            return idAttrVis.get(id).get(attributeName).get(i).get(0);
        }
        return idAttrVis.get(id).get(attributeName).get(id).get(0);
    }

    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException,
            AttributeWrongTypeException {
        exceptionClass(className);
        String id  = name2id.get(className).get(0);
        exceptionAttr(className, attributeName);
        NameableType s = idAttrType.get(id).get(attributeName).get(0);
        if (s instanceof NamedType) {
            NamedType s1 = (NamedType) s;
            if (nameTypeIsValid(s1, Direction.IN)) {
                return s1.getName();
            } else {
                throw new AttributeWrongTypeException(className, attributeName);
            }
        }
        else {
            String t = ((ReferenceType) s).getReferenceId();
            return id2name.get(t);
        }
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(String className,
            String operationName) throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        exceptionClass(className);
        String id = name2id.get(className).get(0);
        List<OperationParamInformation> ans = new ArrayList<>();
        if (!id2opt2id.get(id).containsKey(operationName)) {
            return ans;
        }
        for (String optid : id2opt2id.get(id).get(operationName)) {
            ArrayList<Map<NameableType, Direction>> para = optid2para.get(optid);
            ArrayList<String> key = new ArrayList<>();
            String value = null;
            for (Map<NameableType, Direction> item : para) {
                for (NameableType i : item.keySet()) {
                    if (item.get(i).equals(Direction.IN)) {
                        if (i instanceof NamedType) {
                            NamedType s = (NamedType) i;
                            if (nameTypeIsValid(s, Direction.IN)) {
                                key.add(s.getName());
                            } else {
                                throw new MethodWrongTypeException(className, operationName);
                            }
                        } else if (i instanceof ReferenceType) {
                            key.add(id2name.get(((ReferenceType) i).getReferenceId()));
                        }
                    } else if (item.get(i).equals(Direction.RETURN)) {
                        if (i instanceof NamedType) {
                            NamedType s = (NamedType) i;
                            if (nameTypeIsValid(s, Direction.RETURN)) {
                                value = s.getName();
                            } else {
                                throw new MethodWrongTypeException(className, operationName);
                            }
                        } else if (i instanceof ReferenceType) {
                            value = id2name.get(((ReferenceType) i).getReferenceId());
                        }
                    }
                }
            }
            OperationParamInformation k = new OperationParamInformation(key, value);
            if (ans.contains(k)) {
                throw new MethodDuplicatedException(className, operationName);
            } else {
                ans.add(k);
            }
        }
        return ans;
    }

    private boolean nameTypeIsValid(NamedType s, Direction t) {
        boolean ans;
        String st = s.getName();
        ans = st.equals("byte") || st.equals("short") || st.equals("int")
                || st.equals("long") || st.equals("float") || st.equals("double");
        ans = ans || st.equals("char") || st.equals("boolean") || st.equals("String");
        if (t.equals(Direction.RETURN)) {
            ans = ans || st.equals("void");
        }
        return ans;
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        exceptionClass(className);
        String s = name2id.get(className).get(0);
        if (!idAssociateTo.containsKey(s)) {
            idAssociateTo.put(s, new ArrayList<>());
        }
        List<String> ans = new ArrayList<>();
        ArrayList<String> tmp = idAssociateTo.get(s);
        for (String id : tmp) {
            if (id2Type.get(id).equals(UML_CLASS)) {
                ans.add(id2name.get(id));
            }
        }
        return ans;
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        exceptionClass(className);
        String id = name2id.get(className).get(0);
        String topId = id2TopClass.get(id);
        return id2name.get(topId);
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        exceptionClass(className);
        String id = name2id.get(className).get(0);
        ArrayList<String> strings = idImplementTo.get(id);
        ArrayList<String> ans = new ArrayList<>();
        if (strings == null) {
            return ans;
        }
        for (String s : strings) {
            ans.add(id2name.get(s));
        }
        return ans;
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        exceptionClass(className);
        String id = name2id.get(className).get(0);
        Map<String, Map<String, ArrayList<Visibility>>> p = idAttrVis.get(id);
        List<AttributeClassInformation> ans = new ArrayList<>();
        for (String s : p.keySet()) {
            Map<String, ArrayList<Visibility>> tmp = p.get(s);
            for (String s1 : tmp.keySet()) {
                Visibility v = tmp.get(s1).get(0);
                if (!v.equals(Visibility.PRIVATE)) {
                    AttributeClassInformation t = new AttributeClassInformation(s, id2name.get(s1));
                    ans.add(t);
                }
            }
        }
        return ans;
    }

    private void exceptionClass(String classname)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2id.containsKey(classname)) {
            throw new ClassNotFoundException(classname);
        } else if (name2id.get(classname).size() != 1) {
            throw new ClassDuplicatedException(classname);
        }
    }

    private void exceptionAttr(String classname, String attr)
            throws AttributeNotFoundException, AttributeDuplicatedException {
        Map<String, Map<String, ArrayList<Visibility>>> p =
                idAttrVis.get(name2id.get(classname).get(0));
        if (!p.containsKey(attr)) {
            throw new AttributeNotFoundException(classname, attr);
        } else if (p.get(attr).size() != 1) {
            throw new AttributeDuplicatedException(classname, attr);
        }
    }
}
