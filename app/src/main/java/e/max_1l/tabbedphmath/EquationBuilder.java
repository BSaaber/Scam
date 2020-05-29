package e.max_1l.tabbedphmath;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.TreeMap;

public class EquationBuilder {
    EquationTree left, right;
    String variable;
    static final String LEFT = "LEFT";
    static final String RIGHT = "RIGHT";
    String original;
    ArrayList<Character> sp;

    //конструктор уравнения
    public EquationBuilder(String s) {
        variable = "";
        String t = "" + s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            boolean isnum1 = (int) s.charAt(i - 1) >= (int) '0' && (int) s.charAt(i - 1) <= (int) '9';
            boolean isnum2 = (int) s.charAt(i) >= (int) '0' && (int) s.charAt(i) <= (int) '9';
            boolean f1 = ((int) s.charAt(i - 1) >= (int) 'A' && (int) s.charAt(i - 1) <= (int) 'Z') || ((int) s.charAt(i - 1) >= (int) 'a' && (int) s.charAt(i - 1) <= (int) 'z');
            boolean f2 = ((int) s.charAt(i) >= (int) 'A' && (int) s.charAt(i) <= (int) 'Z') || ((int) s.charAt(i) >= (int) 'a' && (int) s.charAt(i) <= (int) 'z');
            boolean first = f1 || s.charAt(i - 1) == '}' || s.charAt(i - 1) == ')' || isnum1;
            boolean second = f2 || s.charAt(i) == '(' || isnum2;
            boolean not_a_num = !(isnum1 && isnum2);
            if (first && second && not_a_num){
                t += '*';
            }
            t += s.charAt(i);
        }
        s = t;
        original = s;

        int index = s.indexOf('=');
        left = new EquationTree(EquationTree.slice(s,0, index), null);
        right = new EquationTree(EquationTree.slice(s, index + 1, s.length()), null);
        left.trackVariable(variable);
        right.trackVariable(variable);
        sp = specialCharacters();
    }

    //установка выражаемой переменной
    public void setVariable(String variable) {
        this.variable = variable;
    }


    public ArrayList<String> removeImaginaryZero(ArrayList<String> list){
        ArrayList<String> temp = new ArrayList<>();
        for (String s: list) {
            temp.add(EquationTree.CharacterListToString(goRemoveImaginaryZero(EquationTree.stringToClist(s))));
        }
        return temp;
    }

    public ArrayList<Character> goRemoveImaginaryZero(ArrayList<Character> list){
        ArrayList<Character> r = new ArrayList<>();
        ArrayList<Integer> z = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == '0' && i == 0){
                z.add(0);
            }
            else if (list.get(i) == '0' && !Character.isDigit(list.get(i - 1))){
                z.add(i);
                if (list.get(i - 1) != '{' && list.get(i - 1) != '(' && list.get(i - 1) != '=') {
                    z.add(i - 1);
                }
            }
        }
        for (int i = 0; i < list.size(); i++){
            if (!z.contains(i)){
                r.add(list.get(i));
            }
        }
        return r;
    }

    public ArrayList<Character> specialCharacters(){
        ArrayList<Character> r = new ArrayList<>();
        r.add('(');
        r.add(')');
        r.add('*');
        r.add('/');
        r.add('-');
        r.add('+');
        r.add('{');
        r.add('}');
        return r;
    }

    // все несвободные члены налево, остальное направо
    // выносим искомое
    public ArrayList<String> keepOutVariable(){
        ArrayList<String> solution = new ArrayList<>();
        solution.add(convertToLatex());
        String t = left.popCarrier(true, variable) + "+" + right.popCarrier(false, variable);
        refresh();
        right = new EquationTree(right.value + "-(0+" + left.value + ')', null);
        left = new EquationTree(t, null);
        open();
        solution.add(convertToLatex());

        //выносим искомое
        solution.add(takeawayUpdate(variable));
        return solution;
    }

    public void refresh(){
        if (left.value.equals("")){
            left = new EquationTree("0", null);
        }
        String s = left.modernCollect();
        left = new EquationTree(s,null);

        left.bubbleSort();
        s = left.modernCollect();
        left = new EquationTree(s,null);

        if (!left.value.equals(variable)) {
            left.parseMinus();
            left.sortMultipliers(variable);
        }

        s = left.modernCollect();
        left = new EquationTree(s,null);



        left.checkNullable();
        s = left.modernCollect();

        left = new EquationTree(s,null);
        left.trackVariable(variable);

        if (right.value.equals("")){
            right = new EquationTree("0", null);
        }
        s = right.modernCollect();
        right = new EquationTree(s,null);

        right.bubbleSort();
        s = right.modernCollect();
        right = new EquationTree(s,null);

        if (!right.value.equals(variable)) {
            right.parseMinus();
            right.sortMultipliers(variable);
        }
        s = right.modernCollect();
        right = new EquationTree(s,null);

        right.checkNullable();
        s = right.modernCollect();
        right = new EquationTree(s,null);

        right.trackVariable(variable);
    }


    // раскрытие скобок
    public ArrayList<String> open(){
        ArrayList<String> solution = new ArrayList<>();
        //refresh();
        solution.add(convertToLatex());
        left.openBrackets();
        //refresh();
        solution.add(convertToLatex());
        right.openBrackets();
        refresh();
        solution.add(convertToLatex());
        return solution;
    }

    // домножаем обе части на все знаменатели (не бездумно, повторяющиеся не записываем)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> multiplyByTheDenominators(){
        ArrayList<String> solution = new ArrayList<>();
        refresh();

        // нашли все знаменатели левой части
        TreeMap<String, Integer> m = left.findDenominators();

        //нашли и добавили знаменатели правой части
        right.findDenominators().forEach((key, value1) -> {
            if (m.getOrDefault(key, 0) < value1){
                m.put(key, value1);
            }
        });

        TreeMap<String, Integer> m1 = new TreeMap<>(m);
        // домножили обе части на все знаменатели
        if (!m.isEmpty()) {
            right.getRidOfDenominators(m);
            left.getRidOfDenominators(m1);
        }
        refresh();
        solution.add(convertToLatex());

        return solution;
    }

    public String takeawayUpdate(String var){
        refresh();
        left.takeaway();
        refresh();
        left = new EquationTree(var + "*(" + left.value + ')', null);
        refresh();
        return convertToLatex();
    }

    public String division(){
        right = new EquationTree('(' + right.value +")/(" + left.lchild.value + ')', null);
        left = new EquationTree(variable, null);
        refresh();
        return fixNegativity();
    }

    public String fixNegativity(){
        if (left.negativity){
            return variable + "=-(" + right.convertToLatex() + ')';
        }
        return left.convertToLatex() + '=' + right.convertToLatex();
    }

    // удаляем из списка на вывод соседние повторяющиеся позиции
    public ArrayList<String> removeNeighboringCopies(ArrayList<String> list){
        ArrayList<String> r = new ArrayList<>();
        r.add(list.get(0));
        for (int i = 1; i < list.size(); i++){
            if (!list.get(i - 1).equals(list.get(i))){
                r.add(list.get(i));

            }
        }
        return r;
    }

    public String convertToLatex(){
        return left.convertToLatex() + '=' + right.convertToLatex();
    }

    public static String convertToEquationLine(String latex){
        String r = "";
        ArrayList<Character> clist = EquationTree.stringToClist(latex);
        ArrayList<Character> cdot = new ArrayList<>();
        cdot.add('c');
        cdot.add('d');
        cdot.add('o');
        cdot.add('f');
        cdot.add('r');
        cdot.add('a');

        for (Character c:
             clist) {
            if (!cdot.contains(c)){
                if (c == 't'){
                    r += '*';
                }
                else if (c == '}'){
                    r += ")/";
                }
                else if (c == '{'){
                    r += '(';
                }
                else {
                    r += c;
                }
            }
        }



        return r;
    }



    // метод, выражающий из уравнения заданную переменную
    // возвращает список строк, пошагово иллюстрирующий выражение переменной
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> solve(){

        //создаем список, в который будем записывать шаги и записываем в него исходные данные
        ArrayList<String> solution = new ArrayList<>();
        solution.add(convertToLatex());

        // раскрываем скобки
        solution.addAll(open());

        // домножаем на знаменатели
        solution.addAll(multiplyByTheDenominators());

        // слева оставляем произведение переменной на коэффициент, справа - остальные члены
        solution.addAll(keepOutVariable());

        // делим обе части на коэффициент
        solution.add(division());

        //избавляемся от мнимых нулей
        // Важно! Они не исчезают из деревьев, только из строк для вывода
        solution = removeImaginaryZero(solution);

        // удаляем соседние копии
        solution = removeNeighboringCopies(solution);

        return solution;
    }


}

