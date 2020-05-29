package e.max_1l.tabbedphmath;

import android.annotation.TargetApi;
import android.media.audiofx.DynamicsProcessing;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.TreeMap;

class EquationTree {
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";
    // public final static int POSITIVE = 1;
    //public final static int NEGATIVE = 0;
    String value; // Здесь содержим строку
    @Nullable
    EquationTree parent, lchild, rchild;
    private char action;
    private int inflection;
    private boolean carrier;
    boolean negativity;

    // создание дерева
    EquationTree(String value, EquationTree parent) {
        this.carrier = false;
        this.parent = parent;

        // преобразуем строку во избежание ошибок при работе с ней
        ArrayList<Character> clist = stringToClist(value);

        //проверяем наличие и удаляем при надобности крайние скобки
        removeSideBrackets(clist);

        //если крайний левый знак - унарный минус, добавляем '0' в начало списка. С ним разберемся позже
        if (clist.get(0) == '-'){
            clist.add(0,'0');
        }

        //записываем значение в поле value
        this.value = CharacterListToString(clist);

        // устанавливаем значения inflection и action, рекурсивно создаем детей
        setOperation(clist);




    }

    // устанавливаем значения inflection и action, рекурсивно создаем детей
    private void setOperation(ArrayList<Character> clist){
        //определяем позицию знака выполняемого действия в строке. отрицательные - спец. значения
        inflection = findActionPosition(clist);
        if (inflection == -1){
            action = 'c';
        } else if (inflection == -2){
            action = 'v';
        }
        else {
            action = this.value.charAt(inflection);
        }

        //рекурсивно создаем детей
        parse();
    }

    //определяем позицию знака выполняемого действия в строке отрицательные значения - для переменных и констант
    private int findActionPosition(ArrayList<Character> deeds){
        ArrayList<Integer> plus = new ArrayList<>();
        ArrayList<Integer> mult = new ArrayList<>();
        for (int i = deeds.size() - 1; i > -1; i--) {
            Character deed = deeds.get(i);
            if (deed == '+' || deed == '-') {
                if (notInBrackets(value, i)) {
                    plus.add(i);
                }
            }
            if (deed == '*' || deed == '/') {
                if (notInBrackets(value, i)) {
                    mult.add(i);
                }
            }
        }

        if (!plus.isEmpty()){
            return plus.get(0);
        }
        if (!mult.isEmpty()){
            return mult.get(0);
        }

        if ((int) value.charAt(0) >= (int) 'A' && (int) value.charAt(0) <= (int) 'z'){ // значение - переменная
            return -2;
        }

        return -1;
    }

    //если все выражение заключено в скобки - убирает их
    private void removeSideBrackets(ArrayList<Character> clist){
        if (clist.get(0) == '(') {
            for (int i = 1, k = 0; i < clist.size() - 1; i++) {
                if (clist.get(i) == ')'){
                    k++;
                }
                else if (clist.get(i) == '('){
                    k--;
                }
                if (k > 0){
                    return;
                }
            }
        }
        else {
            return;
        }
        clist.remove(0);
        clist.remove(clist.size() - 1);
    }

    //преобразует строку в список Character (аналогичный встроенный метод работает некорректно)
    static ArrayList<Character> stringToClist(String s){
        char [] c = s.toCharArray();
        ArrayList<Character> cl = new ArrayList<>();
        for (char c1 : c) {
            if (c1 != '\u0000') {
                cl.add(c1);
            }
        }
        return cl;
    }

    //рекурсивно создаем детей
    private void parse(){
        if (action != 'c' && action != 'v'){
            lchild = new EquationTree(slice(value, 0, inflection), this);
            rchild = new EquationTree(slice(value, inflection + 1, value.length()), this);
        }
        else {
            lchild = null;
            rchild = null;
        }
    }

    // копирует все поля заданного дерева
    // !!! включая связи с родителем, детьми
    // если хотим перекопировать только поля дерева, используем copy(new EquationTree name);
    private void copy(EquationTree tree, EquationTree parent){
        this.value = tree.value;
        this.parent = parent;
        this.lchild = tree.lchild;
        this.rchild = tree.rchild;
        this.action = tree.action;
        this.inflection = tree.inflection;
        this.carrier = tree.carrier;
        this.negativity = tree.negativity;
    }

    // преобразуем список Character в строку
    static String CharacterListToString(ArrayList<Character> list){
        String s = "";
        for (Character character : list) {
            s = s + character;
        }
        return s;
    }

    // возвращаем срез строки по индексам (включительно, невключительно)
    static String slice(String s, int start, int stop){
        if (start >= stop){
            return "";
        }
        char[] a = new char[stop - start + 1];
        s.getChars(start, stop, a, 0);
        s = String.copyValueOf(a);
        return s;
    }

    // метод подсчета количества символов с в строке
    private int countChar(String s, char c){
        char[] a= s.toCharArray();
        int r = 0;
        for (char c1 : a) {
            if (c1 == c) {
                r++;
            }
        }
        return r;
    }

    // на вход получаем строку и индекс в ней
    // если знак на этой позиции не находится в скобках, вернем true
    private boolean notInBrackets(String s, int index){
        int open = countChar(slice(s, index + 1, s.length()), '(');
        int close = countChar(slice(s, index + 1, s.length()), ')');
        return open >= close;
    }

    // функция не вызывается в программе рекурсивно!
    // если все дерево - одна переменная и она является искомой, вернем ее
    // иначе ищем несвободные члены
    // way отвечает за знаки
    String popCarrier(boolean way, String variable){
        if (lchild == null && value.equals(variable)){
            makeNull();
            if (way){
                return "0+" + variable;
            }
            return "0-" + variable;
        }
        return "0" + goPopCarrier(way, variable);
    }

    private String goPopCarrier(boolean way, String variable){
        switch (action){
            case '*':
                if (rchild.value.equals(variable)){
                    String t = value;
                    makeNull();
                    if (way) {
                        return '+' + t;
                    }
                    return '-' + t;
                }
                return "";
            case '+':
                return lchild.goPopCarrier(way, variable) + rchild.goPopCarrier(way, variable);
            case '-':
                return lchild.goPopCarrier(way, variable) + rchild.goPopCarrier(!way, variable);
            case 'c':
                return "";
            case 'v':
                if (value.equals(variable)){
                    String t = value;
                    makeNull();
                    if (way) {
                        return '+' + t;
                    }
                    return '-' + t;
                }
        }
        return "";
    }

    public void bubbleSort(){
        if (lchild != null){
            if (action == '*'){
                int depth = -1;
                while (0 < depth || depth == -1){
                    depth = goBubbleSort(0, depth);
                }
            }
            else {
                lchild.bubbleSort();
                rchild.bubbleSort();
            }
        }
    }

    public int goBubbleSort(int pos, int depth){
        pos++;
        if (depth == -1 || pos < depth){
            if (rchild.action == '+' || rchild.action == '-'){
                rchild.bubbleSort();
            }
            if (lchild.action == '*'){
                if (0 < lchild.rchild.value.compareTo(rchild.value)){
                    EquationTree t = new EquationTree(rchild.value, lchild);
                    rchild.copy(lchild.rchild, this);
                    lchild.rchild = t;
                }
                return lchild.goBubbleSort(pos, depth);
            }
            else if (0 < lchild.value.compareTo(rchild.value)){
                EquationTree t = new EquationTree(rchild.value, this);
                rchild.copy(lchild, this);
                lchild = t;
            }
            return pos;
        }
        return depth - 1;
    }

    //выводим искомую переменную на верх многочлена (направо)
    void sortMultipliers(String variable){
        if (lchild != null) {
            if (!lchild.value.equals(variable) || !rchild.value.equals(variable)) {
                lchild.sortMultipliers(variable);
                rchild.sortMultipliers(variable);
            }
            if (lchild.value.equals(variable) && action != '-' && action != '/') {
                EquationTree t = lchild;
                lchild = rchild;
                rchild = t;
            }
            if (parent != null && rchild.value.equals(variable)) {
                if (parent.action == '*' && action == '*') { // переносим переменную на ступень вверх
                    EquationTree t = parent.rchild;
                    parent.rchild = rchild;
                    rchild = t;
                }
            }
        }
    }

    // определяет carrier
    boolean trackVariable(String variable){
        if (lchild != null) {
            boolean l = lchild.trackVariable(variable);
            boolean r = rchild.trackVariable(variable);
            if (l || r){
                carrier = true;
                return true;
            }
            else {
                carrier = false;
                return false;
            }
        }
        else if (value.equals(variable)){
            carrier = true;
            return true;
        }
        else {
            carrier = false;
            return false;
        }
    }

    private void changeMinus(){
        if (lchild == null){
            negativity = true;
        }
        else {
            lchild.changeMinus();
        }
    }

    void parseMinus(){
        if (value.length() > 1) {
            char[] ch = value.toCharArray();
            for (int i = 0; i < inflection; i++) {
                if (findMinus(ch, i, lchild)) break;
            }

            for (int i = inflection + 1; i < ch.length; i++) {
                if (findMinus(ch, i, rchild)) break;
            }
        }
    }

    private boolean findMinus(char[] ch, int i, EquationTree rchild) {
        if (ch[i] == '-'){
            rchild.changeMinus();
            return true;
        }
        return ch[i] != '(' && ch[i] != '-';
    }

    // пересобираем всё дерево исходя из строк листов
    String modernCollect(){
        if (action == 'c' || action == 'v' || action == '_'){
            if (negativity){
                return "(-" + value + ')';
            }
            return value;
        }

        if (action == '+' || action == '-'){
            String t;
            boolean z = negativity;
            if (action == '+' && lchild.action == '_'){
                t = rchild.modernCollect();
            }
            else if (action == '+' && rchild.action == '_'){
                t = lchild.modernCollect();
            }
            else if (action == '-' && lchild.action == '_'){
                z = !negativity;
                t = rchild.modernCollect();
            }
            else if (action == '-' && rchild.action == '_'){
                t = lchild.modernCollect();
            }
            else if (rchild.action == '+' || rchild.action == '-') {
                t = lchild.modernCollect() + action + '(' + rchild.modernCollect() + ')';
            }
            else {
                t = lchild.modernCollect() + action + rchild.modernCollect();
            }

            if (parent != null){
                if (parent.action == '*' || parent.action == '/' || negativity){
                    if (z){
                        return "(-" + t + ')';
                    }
                    else {
                        return "(" + t + ')';
                    }
                }
            }
            if (z){
                return "(-" + t + ')';
            }
            return t;
        }
        if (action == '/' && rchild.action == '*'){
            return lchild.modernCollect() + action + '(' + rchild.modernCollect() + ')';
        }

        if (action == '*' || action == '/'){
            return lchild.modernCollect() + action + rchild.modernCollect();
        }
        return "ERROR";
    }

    // узнаем, левый мы ребенок нашего родителя, или правый
    private String getLRPosition(){
        if (parent != null){
            if (parent.lchild == this){
                return LEFT;
            }
            else {
                return RIGHT;
            }
        }
        return "NULL";
    }

    private boolean tModernCollect(){
        if (getLRPosition().equals("RIGHT")){
            String a = slice(parent.value, parent.inflection + 1, parent.value.length());
            int inf = findActionPosition(stringToClist(a));
            return !notInBrackets(a, inf);
        }
        else {
            String a = slice(parent.value, 0, parent.inflection);
            int inf = findActionPosition(stringToClist(a));
            return !notInBrackets(a, inf);
        }
    }

    //раскрытие скобок
    //раскрытие скобок
    void openBrackets(){
        if (lchild != null){

            // опускаемся к низу дерерва
            lchild.openBrackets();
            rchild.openBrackets();

            // скобки существуют, если один из операндов *, / является + или -
            if (action == '*' || action == '/'){
                if (lchild.action == '+' || lchild.action == '-'){
                    lchild.goOpenBrackets(rchild, action, EquationBuilder.RIGHT);
                    copy(lchild, parent);
                    refresh(parent);
                }
                else if ((rchild.action == '+' || rchild.action == '-') && action == '*'){
                    rchild.goOpenBrackets(lchild, action, EquationBuilder.LEFT);
                    copy(rchild, parent);
                    refresh(parent);
                }
            }



            // или, если правый ребенок при вычитании не константа, переменная или произведение
            if (action == '-'){
                if (rchild.action == '+' || rchild.action == '-'){
                    rchild.reverseMinus();
                    action = '+';
                    refresh(parent);
                }
            }
        }
        refresh(parent);
    }

    //раскрываем скобку со стоящим перед ней минусом
    private void reverseMinus(){
        if (action == '+'){
            action = '-';
            lchild.reverseMinus();
        }
        else if (action == '-'){
            action = '+';
            lchild.reverseMinus();
        }
    }

    // изменяем дерево, у которого вызвали функцию, выполняя с каждым его ребенком операцию act с операндом multiplier
    private void goOpenBrackets(EquationTree multyplier, char act, String multplace){
        if (action == '*' || action == 'c' || action == 'v' || action == '/'){ //а если '/'?
            if (multplace.equals(EquationBuilder.RIGHT)) {
                lchild = new EquationTree(value, this);
                rchild = new EquationTree(multyplier.value, this);
            }
            else {
                rchild = new EquationTree(value, this);
                lchild = new EquationTree(multyplier.value, this);
            }
            action = act;

        }
        else {
            lchild.goOpenBrackets(multyplier, act, multplace);
            rchild.goOpenBrackets(multyplier, act, multplace);
            if (act == '*') {
                lchild.openBrackets();
                rchild.openBrackets();
            }
        }
    }


    // обновление данных
    private void refresh(EquationTree p){
        copy( new EquationTree(modernCollect(),p), p);
    }

    // удаляет из многочлена искомую переменную
    // одна из функций, используемых для выноса искомой переменной за скобки
    void takeaway(){
        switch (action){
            case '+':
            case '-':
                rchild.takeaway();
                lchild.takeaway();
                break;
            case '*':
                copy(lchild, parent);
                break;
            case 'v':
                copy(new EquationTree("1", parent), parent);
                break;
        }
    }

    //составляем карту всех знаменателей дерева, учитывая их количество (сложно)
    @RequiresApi(api = Build.VERSION_CODES.N)
    TreeMap<String, Integer> findDenominators(){
        TreeMap<String, Integer> dmap = new TreeMap<>();
        if (action != 'c' && action != 'v'){
            //рекурсивно находим все знаменатели и сверяемся, что не берем одинаковые
            dmap.putAll(lchild.findDenominators());
            rchild.findDenominators().forEach((key, value1) -> {
                if (dmap.getOrDefault(key, 0) < value1){
                    dmap.put(key, value1);
                }
            });
        }

        //выход из рекурсии
        //увидели деление на константу или переменную - добавили в карту
        //проверка на c, v излишняя - все скобки уже раскрыты и других делителей не может быть
        //но она может потребоваться в других задачах функции, если они появятся
        if (action == '/' /*&& (rchild.action == 'c' || rchild.action == 'v')*/){
            dmap.merge(rchild.value, 1, (a, b) -> a + b);
        }
        return dmap;
    }

    //домножаем все одночлены на переданные делители
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    void getRidOfDenominators(TreeMap<String, Integer> denominators){
        if (action == '+' || action == '-'){
            lchild.getRidOfDenominators(new TreeMap<>(denominators));
            rchild.getRidOfDenominators(new TreeMap<>(denominators));
        }

        //убираем из списка делителей правого ребенка
        else if (action == '/'){
            denominators.remove(rchild.value);
            this.copy(lchild, lchild.parent);
            getRidOfDenominators(new TreeMap<>(denominators));
        }

        //т. к. обходим многочлены "справа налево", проверять правого ребенка не нужно
        else if (action == '*'){
            lchild.getRidOfDenominators(denominators);
        }

        // выход из рекурсии, домножаем на оставшиеся делители
        else if (action == 'c' || action == 'v'){
            ArrayList<String> slist = StringMapToList(denominators);
            String s = String.join(")*(", slist);
            if (!s.equals("")) {
                this.copy(new EquationTree(value + "*(" + s + ')', parent), parent);
            }
        }
    }

    // превращаем карту типа {"одночлен": количество...} в единый многочлен (String)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> StringMapToList(TreeMap<String, Integer> m){
        ArrayList<String> s = new ArrayList<>();

        m.forEach((key, value1) -> {
            for (int i = 0; i < value1; i++){
                s.add(key);
            }
        });
        return s;
    }


    // находим и удаляем нули
    void checkNullable(){
        if (action == 'c'){
            if (value.equals("0")){
                if (parent != null) {
                    parent.delete();
                }
            }
        }
        else if (lchild != null){
            lchild.checkNullable();
            if (rchild != null) {
                rchild.checkNullable();
            }
        }
    }

    // копируем создаваемое здесь же дерево, взяв за основу строку "0" и нынешнего родителя
    private void makeNull(){
        copy(new EquationTree("0", parent), parent);
    }


    // удаляем ноль
    private void delete(){
        switch (action){
            case '*':
                makeNull();
                if (parent != null) {
                    parent.delete();
                }
                break;
            case '/':
                if (lchild.value.equals("0")){
                    makeNull();
                }
                else {
                    System.out.println("Division by zero!");
                }
                break;
            case '+':
                if (lchild.value.equals("0")){
                    copy(rchild, parent);
                }
                else {
                    copy(lchild, parent);
                }
                break;
            case '-':
                if (rchild.value.equals("0")){
                    copy(lchild, parent);
                }
                else {
                    // c мнимым нулем не убираем
                    // с ним работать удобнее
                    // просто будем удалять его из строк вывода в отдельном методе
                    copy(new EquationTree("(-" + rchild.value + ")", parent), parent);
                }
                break;
        }

    }

    public String convertToLatex(){
        String tl;
        String tr;
        switch (action){
            case 'c':
            case 'v':
                return value;
            case '*':
                if (lchild.needBrackets()){
                    tl = '(' + lchild.convertToLatex() + ')';
                }
                else {
                    tl = lchild.convertToLatex();
                }

                if (rchild.needBrackets()){
                    tr = '(' + rchild.convertToLatex() + ')';
                }
                else {
                    tr = rchild.convertToLatex();
                }
                if (lchild.rchild != null){
                    if (lchild.rchild.action == 'c' && rchild.action == 'c'){
                        return tl + "\\cdot" + tr;
                    }
                }
                else if (lchild.action == 'c' && rchild.action == 'c'){
                    return tl + "\\cdot" + tr;
                }
                return tl + tr;
            case '/':
                if (lchild.needBrackets()){
                    tl = '(' + lchild.convertToLatex() + ')';
                }
                else {
                    tl = lchild.convertToLatex();
                }

                if (rchild.needBrackets()){
                    tr = '(' + lchild.convertToLatex() + ')';
                }
                else {
                    tr = rchild.convertToLatex();
                }
                return "\\frac {" + tl + "} {" + tr + "}";
            case '+':
                return lchild.convertToLatex() + "+" + rchild.convertToLatex();
            case '-':
                return lchild.convertToLatex() + "−" + rchild.convertToLatex();

        }
        return "ErRoR";
    }

    private boolean needBrackets(){
        if (parent != null){
            return (action == '+' || action == '-') && (parent.action == '*');
        }
        return false;
    }

}
