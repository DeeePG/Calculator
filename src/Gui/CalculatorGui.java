package Gui;

import Constants.CommonConstants;
import Service.CalculatorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGui extends JFrame implements ActionListener {
    private final SpringLayout springLayout = new SpringLayout();
    private final CalculatorService calculatorService;

    // display field
    private JTextField displayField;
    // buttons
    private JButton[] buttons;
    // flags
    private boolean pressedOperator = false;
    private boolean pressedEquals = false;


    public CalculatorGui(){
        super(CommonConstants.APP_NAME);
        setSize(CommonConstants.APP_SIZE[0], CommonConstants.APP_SIZE[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(springLayout);

        calculatorService = new CalculatorService();

        addGuiComponents();
    }

    public void addGuiComponents(){
        // add display components
        addDisplayFieldComponents();

        // add button components
        addButtonComponents();
    }

    public void addDisplayFieldComponents(){
        JPanel displayFieldPanel = new JPanel();
        displayField = new JTextField(CommonConstants.TEXTFIELD_LENGTH);
        displayField.setFont(new Font("Dialog", Font.PLAIN, CommonConstants.TEXTFIELD_FONTSIZE));
        displayField.setEditable(false);
        displayField.setText("0");
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);

        displayFieldPanel.add(displayField);

        this.getContentPane().add(displayFieldPanel);
        springLayout.putConstraint(SpringLayout.NORTH, displayFieldPanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, displayFieldPanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);
    }

    public void addButtonComponents(){
        GridLayout gridLayout = new GridLayout(CommonConstants.BUTTON_ROWCOUNT, CommonConstants.BUTTON_COLCOUNT);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(gridLayout);
        buttons = new JButton[CommonConstants.BUTTON_COUNT];
        for(int i = 0; i < CommonConstants.BUTTON_COUNT; i++){
            JButton button = new JButton(getButtonLabel(i));
            button.setFont(new Font("Dialog", Font.PLAIN, CommonConstants.BUTTON_FONTSIZE));
            button.addActionListener(this);

            buttonPanel.add(button);
        }

        gridLayout.setHgap(CommonConstants.BUTTON_HGAP);
        gridLayout.setVgap(CommonConstants.BUTTON_VGAP);

        this.getContentPane().add(buttonPanel);

        springLayout.putConstraint(SpringLayout.NORTH, buttonPanel, CommonConstants.BUTTON_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonPanel, CommonConstants.BUTTON_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);
    }
    public String getButtonLabel(int buttonIndex){
        return switch (buttonIndex) {
            case 0 -> "7";
            case 1 -> "8";
            case 2 -> "9";
            case 3 -> "/";
            case 4 -> "4";
            case 5 -> "5";
            case 6 -> "6";
            case 7 -> "x";
            case 8 -> "1";
            case 9 -> "2";
            case 10 -> "3";
            case 11 -> "-";
            case 12 -> "0";
            case 13 -> ".";
            case 14 -> "+";
            case 15 -> "=";
            default -> "";
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonCommand = e.getActionCommand();
        if(buttonCommand.matches("[0-9]")){
            if(pressedEquals || pressedOperator || displayField.getText().equals("0"))
                displayField.setText(buttonCommand);
            else
                displayField.setText(displayField.getText() + buttonCommand);

            // update flags
            pressedOperator = false;
            pressedEquals = false;
        }else if(buttonCommand.equals("=")){
            // calculate
            calculatorService.setNum2(Double.parseDouble(displayField.getText()));

            double result = switch (calculatorService.getMathSymbol()) {
                case '+' -> calculatorService.add();
                case '-' -> calculatorService.subtract();
                case '/' -> calculatorService.divide();
                case 'x' -> calculatorService.multiply();
                default -> 0;
            };

            // update the display field
            displayField.setText(Double.toString(result));

            // update flags
            pressedEquals = true;
            pressedOperator = false;

        }else if(buttonCommand.equals(".")){
            if(!displayField.getText().contains(".")){
                displayField.setText(displayField.getText() + buttonCommand);
            }
        }else{ // operator
            if(!pressedOperator)
                calculatorService.setNum1(Double.parseDouble(displayField.getText()));

            calculatorService.setMathSymbol(buttonCommand.charAt(0));

            // update flags
            pressedOperator = true;
            pressedEquals = false;
        }
    }
}