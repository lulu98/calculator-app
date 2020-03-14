package com.example.activitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputField = (TextView)findViewById(R.id.inputView);
        instantiateBasicButton((Button)findViewById(R.id.button0),"0");
        instantiateBasicButton((Button)findViewById(R.id.button1),"1");
        instantiateBasicButton((Button)findViewById(R.id.button2),"2");
        instantiateBasicButton((Button)findViewById(R.id.button3),"3");
        instantiateBasicButton((Button)findViewById(R.id.button4),"4");
        instantiateBasicButton((Button)findViewById(R.id.button5),"5");
        instantiateBasicButton((Button)findViewById(R.id.button6),"6");
        instantiateBasicButton((Button)findViewById(R.id.button7),"7");
        instantiateBasicButton((Button)findViewById(R.id.button8),"8");
        instantiateBasicButton((Button)findViewById(R.id.button9),"9");
        instantiateBasicButton((Button)findViewById(R.id.buttonBracketOpen),"(");
        instantiateBasicButton((Button)findViewById(R.id.buttonBracketClose),")");
        instantiateBasicButton((Button)findViewById(R.id.buttonPlus),"+");
        instantiateBasicButton((Button)findViewById(R.id.buttonMinus),"-");
        instantiateBasicButton((Button)findViewById(R.id.buttonDiv),"/");
        instantiateBasicButton((Button)findViewById(R.id.buttonMult),"*");
        instantiateBasicButton((Button)findViewById(R.id.buttonDecimalPoint),".");
        instantiateBasicButton((Button)findViewById(R.id.buttonExponent),"^");

        Button buttonAC = (Button)findViewById(R.id.buttonAC);
        buttonAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputField.setText("");
            }
        });

        Button buttonResult = (Button)findViewById(R.id.buttonResult);
        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exp = inputField.getText().toString();
                try{
                    if(exp.length() > 0){
                        List<String> infix = prepareString(exp);
                        List<String> postfix = infixToPostfix(infix);
                        double result = evaluatePostfixExpression(postfix);
                        inputField.setText(String.valueOf(result));
                        //DecimalFormat numberFormat = new DecimalFormat("#.0000000");
                        //inputField.setText(numberFormat.format(result));
                    }
                }catch (Exception e){
                    inputField.setText("");
                    Log.i("Exception","Invalid Expression!");
                }
            }
        });
    }

    protected void instantiateBasicButton(Button button, final String symbol){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputField.setText(inputField.getText().toString() + symbol);
            }
        });
    }

    protected List<String> prepareString(String exp){
        List<String> infixNotation = new ArrayList<>();
        String temp = "";
        for(int i = 0; i < exp.length(); i++){
            if(	exp.charAt(i)=='+' || exp.charAt(i)=='-' ||
                    exp.charAt(i)=='*' || exp.charAt(i)=='/' ||
                    exp.charAt(i)=='(' || exp.charAt(i)==')' ||
                    exp.charAt(i)=='^'){
                if(temp.length() > 0) {
                    infixNotation.add(temp);
                    temp = "";
                }
                infixNotation.add(String.valueOf(exp.charAt(i)));
            }else {
                temp += exp.charAt(i);
            }
        }
        if(temp.length()>0) {
            infixNotation.add(temp);
        }
        return infixNotation;
    }

    protected List<String> infixToPostfix(List<String> exp){
        Stack<String> stack = new Stack<String>();
        List<String> result = new ArrayList<>();

        for(int i = 0; i < exp.size(); i++) {
            String c = exp.get(i);
            if(isOperand(c)) {
                result.add(c);
            }else if(c.equals("(")) {
                stack.push(c);
            }else if(c.equals(")")) {
                while(	stack.size() != 0 &&
                        !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                if(	stack.size() != 0 && !stack.peek().equals("(")) {
                    Log.i("Something went wrong","Invalid Expression");
                }else {
                    stack.pop();
                }
            }else {
                while(	stack.size() != 0 && getPrecedence(c) <= getPrecedence(stack.peek())) {
                    result.add(stack.pop());
                }
                stack.push(c);
            }
        }
        while(stack.size() != 0) {
            result.add(stack.pop());
        }

        return result;
    }

    protected boolean isOperand(String op) {
        return !(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("(") || op.equals(")") || op.equals("^"));
    }

    protected boolean isOperator(String op) {
        return (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("^"));
    }

    protected int getPrecedence(String op) {
        switch(op.charAt(0)) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    protected double evaluatePostfixExpression(List<String> exp){
        Stack<Double> stack = new Stack<Double>();
        for(int i = 0 ; i < exp.size(); i++) {
            String c = exp.get(i);
            if(isOperand(c)) {
                stack.push(Double.parseDouble(c));
            }else if(isOperator(c)) {
                double op2 = stack.pop();
                double op1 = stack.pop();
                double result = perform(exp.get(i),op1,op2);
                stack.push(result);
            }
        }
        return stack.pop();
    }

    protected double perform(String operation, double op1, double op2) {
        switch(operation) {
            case "+":
                return op1 + op2;
            case "-":
                return op1 - op2;
            case "*":
                return op1 * op2;
            case "/":
                return op1 / op2;
            case "^":
                return Math.pow(op1, op2);
        }
        return 0;
    }
}
