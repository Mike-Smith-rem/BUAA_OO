package src;

import java.util.ArrayList;

public class Term extends Factor implements Derivation {
    private ArrayList<Factor> factors = new ArrayList<>();

    //    /**
    //     * 通过字符串初始化
    //     * @param str 传入字符串
    //     */
    //    public src.Term (String str){
    //        String power = "[+-]?x(\\*\\*[+-]?\\d+)?";
    //        String triangle = "[+-]?(sin|cos)\\{x}(\\*\\*[+-]?\\d+)?";
    //        String normalNumber = "\\d+";
    //        Pattern patternT = Pattern.compile(triangle);
    //        Pattern patternP = Pattern.compile(power);
    //        Pattern patternN = Pattern.compile(normalNumber);
    //        Matcher matcherT = patternT.matcher(str);
    //        while (matcherT.find()){
    //            src.Triangle t = new src.Triangle(matcherT.group());
    //            factors.add(t);
    //            /*
    //             * 优化代码
    //             */
    //            //标记是否存在
    ////            int flag = 0;
    ////            for (src.Factor value : factors){
    ////                if (value.getClass().equals(src.Triangle.class)){
    ////                    if (t.getType().equals(((src.Triangle) value).getType())){
    ////
    ////                    }
    ////                }
    ////            }
    //        }
    //        str = str.replaceAll(triangle,"t");
    //        Matcher matcherP = patternP.matcher(str);
    //        while (matcherP.find()){
    //            src.Power p = new src.Power(matcherP.group());
    //            factors.add(p);
    //            /*
    //             * 优化代码
    //             */
    //        }
    //        str = str.replaceAll(power,"p");
    //        Matcher matcherN = patternN.matcher(str);
    //        while (matcherN.find()){
    //            src.NormalNumber n = new src.NormalNumber(matcherN.group());
    //            factors.add(n);
    //            /*
    //             * 优化代码
    //             */
    //        }
    //    }

    /**
     * 以下三个函数同质
     * 用于自底向上
     *
     */
    public Term(ArrayList<Factor> arrayList) {
        factors.addAll(arrayList);
    }

    /**
     * 专门用于传递克隆
     */
    public Term() {
        super();
    }

    //    /**
    //     * 链表结构（自返）
    //     * 自底向上
    //     * 定义导数类型
    //     * @return 自反
    //     */
    //    public src.Term setDerivation(){
    //        //自底向上得到表达式类型
    //        ArrayList<src.Term> termArrayList = new ArrayList<>();
    //        for (src.Factor f : factors){
    //            //自我复制，同时避免循环中remove
    //            //src.Term term = this.clone(this);
    //            src.Term term = new src.Term();
    //            term.factors.addAll(this.factors);
    //            /*src.Derivation
    //            * 假设FactorArraylist为f(x)和g(x)
    //            * */
    //            //得到当前项f'(x)
    //            src.Term term1 = (src.Term) f.setDerivation().derivation;
    //            //删除因子f(x)
    //            term.factors.remove(f);
    //            /*将f'(x)中所有因子加入Arraylist
    //            * FactorArraylist：(src.Term)f'(x),(src.Factor)g(x)
    //            * */
    //            term.factors.addAll(term1.factors);
    //            //将新得到的Term(含有FactorArraylist)加入TermArraylist
    //            termArrayList.add(term);
    //            /*
    //              循环最后得到TermArray[1]:f'(x),g(x)
    //                        TermArray[2]:f(x),g'(x)
    //             */
    //        }
    //        /*
    //         * 自底向上
    //         */
    //        this.derivation = new Expression(termArrayList);
    //        return this;
    //    }

    //    /**
    //     * 自我复制
    //     * @param term
    //     * @return
    //     */
    //    public src.Term clone(src.Term term){
    //        src.Term term1 = new src.Term();
    //        term1.factors.addAll(term.factors);
    //        return term1;
    //    }
    //
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Factor factor : factors) {
            stringBuilder.append(factor.toString()).append("*");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        //        /*
        //         * 优化合并
        //         */
        //        //指数分成四个部分
        //        int numberOfSubtract = 0;
        //        BigInteger expOfTriangleSine = BigInteger.ZERO;
        //        BigInteger expOfTriangleCosine = BigInteger.ZERO;
        //        BigInteger expOfPower = BigInteger.ZERO;
        //        BigInteger number = BigInteger.ONE;
        //        for (src.Factor value : factors){
        //            //类型为三角函数
        //            if (value.getClass().equals(src.Triangle.class)){
        //                src.Triangle t = (src.Triangle) value;
        //                if (t.getType().equals("sin")){
        //                    expOfTriangleSine = expOfTriangleSine.add(value.exp);
        //                }
        //                else {
        //                    expOfTriangleCosine = expOfTriangleCosine.add(value.exp);
        //                }
        //            }
        //            //类型为幂函数
        //            else if (value.getClass().equals(src.Power.class)){
        //                src.Power p = (src.Power) value;
        //                expOfPower = expOfPower.add(p.exp);
        //            }
        //            //类型为常量
        //            else if (value.getClass().equals(src.NormalNumber.class)){
        //                src.NormalNumber n = (src.NormalNumber) value;
        //                number = number.multiply(n.getBigInteger());
        //            }
        //            numberOfSubtract = numberOfSubtract + (value.symbols.equals("-") ? 1 : 0);
        //        }
        //        //首先应该是对常数判断，并添上符号
        //        if (number.equals(BigInteger.ZERO)){
        //            return "0";
        //        }
        //        else {
        //            stringBuilder.append(numberOfSubtract%2 == 0 ? "" : "-")
        //                         .append(number)
        //                         .append("*");
        //        }
        //        //其次判断三角函数
        //        src.Triangle sine = new src.Triangle("","sin",expOfTriangleSine);
        //        stringBuilder.append(sine.toString())
        //                     .append("*");
        //        src.Triangle cosine = new src.Triangle("","cos",expOfTriangleCosine);
        //        stringBuilder.append(cosine.toString())
        //                     .append("*");
        //        //然后是幂函数
        //        src.Power power = new src.Power("",expOfPower);
        //        stringBuilder.append(power.toString());
        //        /*
        //         *优化完成
        return stringBuilder.toString();
    }
}
