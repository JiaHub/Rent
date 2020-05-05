package com.zhh.rent.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhh.rent.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class CalculateActivity extends AppCompatActivity {

    TextView mEtContent;
    TextView mTvResult;
    ImageButton mIBtnBack;

    Button mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9;
    Button mBtnAdd, mBtnReduce, mBtnRide, mBtnDivide;
    Button mBtnClear, mBtnPoint, mBtnEqual, mBtnRemainder;

    StringBuilder content = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_activity);
        initView();
        btnClickListener();
    }

    private void initView() {
        mEtContent = findViewById(R.id.calculate_content);
        mTvResult = findViewById(R.id.calculate_result);

        content.append(mEtContent.getText().toString());

        mBtn0 = findViewById(R.id.number_zero);
        mBtn1 = findViewById(R.id.number_one);
        mBtn2 = findViewById(R.id.number_tow);
        mBtn3 = findViewById(R.id.number_three);
        mBtn4 = findViewById(R.id.number_four);
        mBtn5 = findViewById(R.id.number_five);
        mBtn6 = findViewById(R.id.number_six);
        mBtn7 = findViewById(R.id.number_seven);
        mBtn8 = findViewById(R.id.number_eight);
        mBtn9 = findViewById(R.id.number_nine);

        mBtnAdd = findViewById(R.id.operator_plus);
        mBtnReduce = findViewById(R.id.operator_reduce);
        mBtnRide = findViewById(R.id.operator_ride);
        mBtnDivide = findViewById(R.id.operator_divide);
        mBtnRemainder = findViewById(R.id.operator_remainder);

        mIBtnBack = findViewById(R.id.operator_back);
        mBtnClear = findViewById(R.id.operator_clear);
        mBtnPoint = findViewById(R.id.operator_point);
        mBtnEqual = findViewById(R.id.operator_equal);
    }

    private void btnClickListener() {
        //数字监听
        mBtn0.setOnClickListener(new numberListener());
        mBtn1.setOnClickListener(new numberListener());
        mBtn2.setOnClickListener(new numberListener());
        mBtn3.setOnClickListener(new numberListener());
        mBtn4.setOnClickListener(new numberListener());
        mBtn5.setOnClickListener(new numberListener());
        mBtn6.setOnClickListener(new numberListener());
        mBtn7.setOnClickListener(new numberListener());
        mBtn8.setOnClickListener(new numberListener());
        mBtn9.setOnClickListener(new numberListener());
        //加减乘除 取余
        mBtnAdd.setOnClickListener(new operatorListener());
        mBtnReduce.setOnClickListener(new operatorListener());
        mBtnRide.setOnClickListener(new operatorListener());
        mBtnDivide.setOnClickListener(new operatorListener());
        mBtnRemainder.setOnClickListener(new operatorListener());
        //功能键
        mIBtnBack.setOnClickListener(new toolListener());
        mBtnClear.setOnClickListener(new toolListener());
        mBtnPoint.setOnClickListener(new toolListener());
        mBtnEqual.setOnClickListener(new toolListener());
    }

    private class numberListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.number_zero:
                    addContent("0");
                    break;
                case R.id.number_one:
                    addContent("1");
                    break;
                case R.id.number_tow:
                    addContent("2");
                    break;
                case R.id.number_three:
                    addContent("3");
                    break;
                case R.id.number_four:
                    addContent("4");
                    break;
                case R.id.number_five:
                    addContent("5");
                    break;
                case R.id.number_six:
                    addContent("6");
                    break;
                case R.id.number_seven:
                    addContent("7");
                    break;
                case R.id.number_eight:
                    addContent("8");
                    break;
                case R.id.number_nine:
                    addContent("9");
                    break;
                default:
                    break;
            }
        }
    }

    private class operatorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.operator_plus:
                    addContent("+");
                    break;
                case R.id.operator_reduce:
                    addContent("-");
                    break;
                case R.id.operator_ride:
                    addContent("×");
                    break;
                case R.id.operator_divide:
                    addContent("÷");
                    break;
                case R.id.operator_remainder:
                    addContent("%");
                    break;
            }
        }
    }

    private class toolListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.operator_clear:
                    clearContent();
                    break;
                case R.id.operator_back:
                    backContent();
                    break;
                case R.id.operator_equal:
                    getResult(true);
                    break;
                case R.id.operator_point:
                    addContent(".");
                    break;
            }
        }
    }

    private void clearContent() {
        if (content.length() != 0) content.delete(0, content.length());  //左闭右开
        mEtContent.setText("");
        mTvResult.setText("");
    }

    /**
     * 还未添加结果的处理
     */
    private void backContent() {
        if (content.length() > 0) content.deleteCharAt(content.length() - 1);  //参数是索引
        if (content.length() > 0) {
            String result = getResult(false);
            if (result != null) mTvResult.setText(result);
        }
        if (content.length() == 0) mTvResult.setText("");
        mEtContent.setText(content.toString());

    }

    private void addContent(String str) {
        if (content.length() == 0) {
            if (firstEnterJudgeToAdd(str)) content.append(str);
        } else {
            if (lastEnterJudgeToAdd(str)) content.append(str);
        }
        mEtContent.setText(content.toString());
        if (!isOperator(str)) mTvResult.setText(getResult(false));
    }

    private boolean isOperator(String str) {
        return str.equals("+") || str.equals("÷") || str.equals("×") || str.equals("=") || str.equals("%") || str.equals(".") || str.equals("-");
    }

    private boolean firstEnterJudgeToAdd(String str) {
        if (content.length() == 0) {
            switch (str) {
                case "-":
                    break;
                case ".":
                    content.append("0");
                    break;
                default:
                    if (isOperator(str)) return false;
                    break;
            }
        }
        return true;
    }

    private boolean lastEnterJudgeToAdd(String str) {
        boolean lastCharIsOperator = isOperator(String.valueOf(content.charAt(content.length() - 1)));
        if (lastCharIsOperator && isOperator(str)) {  //最后输入是操作符
            //并且不是第一个字符的时候替换
            if (content.length() != 1) content.replace(content.length() - 1, content.length(), str);
            return false;
        }
        return true;
    }

    private String getResult(boolean isEqual) {
        try {
            Queue<Float> numbers = new LinkedList<>();
            Queue<String> operators = new LinkedList<>();
            int startIndex = 0;
            String str;
            for (int index = 0; index < content.length(); index++) {
                str = String.valueOf(content.charAt(index));
//                if (index==0 && str.equals("-")) continue;    //break跳到循环外，continue进行下一轮
                if (str.equals("-")) {
                    if (index != 0) {
                        numbers.add(Float.parseFloat(content.substring(startIndex, index)));
                        operators.add("+");
                        startIndex = index;
                    }
                    continue;
                }
                if (isOperator(str) && !str.equals(".")) {        //非最后字符时，遇操作符就分割
                    operators.add(str);
                    numbers.add(Float.parseFloat(content.substring(startIndex, index)));
                    startIndex = index + 1;
                }
                if (index == content.length() - 1) {                  //最后字符
                    if (isOperator(str) && !str.equals(".")) {      //有操作符分割
                        operators.add(str);
                        numbers.add(Float.parseFloat(content.substring(startIndex, index)));
                    }
                    numbers.add(Float.parseFloat(content.substring(startIndex)));   //无操作符全加入
                }
            }
            //这里写计算器计算逻辑
            return isEqual ? calculateByQueue(numbers, operators, true) : calculateByQueue(numbers, operators);
        } catch (ArithmeticException e) {
            Log.e("RentCalculateGet", e.getMessage());
            mTvResult.setText("错误");
            return "错误";
        } catch (Exception e) {
            Log.e("RentCalculateGet", e.getMessage());
            return "错误";
        }
    }

    private boolean isPrior(String str) {
        return (str.equals("÷") || str.equals("%") || str.equals("×"));
    }

    /**
     * 该算法，算漏了最后的输入,问题出加入队列时漏判了内容，已解决
     * 该算法未考虑优先级，改动队列无效，并不能解决优先级问题，例如8*2-14/7
     * 基本计算已经在方案二实现
     * @param numbers
     * @param operators
     * @return
     */
    private String calculateByQueue(Queue<Float> numbers, Queue<String> operators) {
        if (operators.size() == 0) {
            String onlyNumberResult = numbers.poll().toString();
            return onlyNumberResult.contains(".0") ? onlyNumberResult.replace(".0", "") : onlyNumberResult;
        }
        try {
            //方案一 没有优先级
//            Float calculate_result = numbers.poll();
//            while(numbers.size()>0){   //此处需要改进
//                calculate_result = calculateByOperator(calculate_result,numbers.poll(),operators.poll());
//            }
//            String result = calculate_result.toString();
//            return result.contains(".0")?result.replace(".0",""):result;

            //方案二 先乘除后加减，合二为一
            Stack<Float> numberStack = new Stack<>();
            Log.e("Numbers", "队列" + numbers.toString());
            Log.e("Numbers", "操作队列" + operators.toString());
            int index = 0;
            while (operators.size() != 0) {
                String operator = operators.poll();
                if (isPrior(operator)) {       //是加号就不管
                    if (index == 0) {
                        numberStack.add(calculateByOperator(numbers.poll(), numbers.poll(), operator));
                        index+=1;
                        continue;
                    }
                    //由于存在取余操作，所以位置不能乱
                    numberStack.add(calculateByOperator(numberStack.pop(),numbers.poll() , operator));  //是高级的，且不是第一个，就取数值栈顶元素，与队列头部元素计算
                } else {
                    if (!numbers.isEmpty()) numberStack.add(numbers.poll());     //这里需要一个一个的加入
                }
                index += 1;
            }
//            numberStack.addAll(numbers);  //清理库存
            Float calculateResult = 0.0f;
            Log.e("Numbers","栈"+numberStack.toString());
            while (!numberStack.empty()) {
                Float f = numberStack.pop();
                calculateResult = calculateResult + f;
            }
            String result = calculateResult.toString();
            return result.contains(".0") ? result.replace(".0", "") : result;
        } catch (Exception e) {
            Log.e("RentCalculateByQueue", e.getMessage());
            return "错误";
        }
    }

    private String calculateByQueue(Queue<Float> numbers, Queue<String> operators, Boolean isEqual) {
        String result = calculateByQueue(numbers, operators);
        if (isEqual) {
            mTvResult.setText("");
            content.delete(0, content.length());
            if (!result.equals("错误")) {
                content.append(result.contains(".0") ? result.replace(".0", "") : result);
                mEtContent.setText(content);
            }
        }
        return result;
    }

    /* 运算符 + - × ÷ */
    private float calculateByOperator(Float param1, Float param2, String operator) {
        Float result = 0f;
        switch (operator) {
            case "+":
                result = param1 + param2;
                break;
            case "-":
                result = param1 - param2;
                break;
            case "×":
                result = param1 * param2;
                break;
            case "÷":
                result = param1 / param2;
                break;
            case "%":
                result = param1 % param2;
            default:
                break;
        }
        return result;
    }

}
