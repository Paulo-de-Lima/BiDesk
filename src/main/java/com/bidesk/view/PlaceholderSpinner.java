package com.bidesk.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceholderSpinner extends JSpinner {
    private String placeholder;
    private Color placeholderColor = new Color(150, 150, 150);
    private boolean showingPlaceholder;
    private JTextField spinnerField;

    public PlaceholderSpinner(String placeholder, SpinnerNumberModel model) {
        super(model);
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        
        // Configurar o campo de texto interno
        JComponent editor = getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            spinnerField = ((JSpinner.DefaultEditor) editor).getTextField();
            spinnerField.setText(placeholder);
            spinnerField.setForeground(placeholderColor);
            
            spinnerField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    // Placeholder continua visível até o usuário digitar
                }

                @Override
                public void focusLost(FocusEvent e) {
                    Object value = getValue();
                    if (value != null && value instanceof Number) {
                        int intValue = ((Number) value).intValue();
                        if (intValue == 0) {
                            spinnerField.setText(placeholder);
                            spinnerField.setForeground(placeholderColor);
                            showingPlaceholder = true;
                        }
                    }
                }
            });
            
            // Listener para detectar quando o usuário começa a digitar
            spinnerField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    checkAndRemovePlaceholder();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    checkAndRestorePlaceholder();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    checkAndRestorePlaceholder();
                }
            });
            
            // Listener para teclas pressionadas
            spinnerField.addKeyListener(new java.awt.event.KeyListener() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (showingPlaceholder && e.getKeyChar() != java.awt.event.KeyEvent.VK_BACK_SPACE && 
                        e.getKeyChar() != java.awt.event.KeyEvent.VK_DELETE && 
                        Character.isDigit(e.getKeyChar())) {
                        spinnerField.setText("");
                        spinnerField.setForeground(Color.BLACK);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {}

                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {}
            });
            
            // Listener para quando o valor muda pelo spinner
            addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        Object value = getValue();
                        if (value != null && value instanceof Number) {
                            int intValue = ((Number) value).intValue();
                            if (intValue == 0 && !spinnerField.hasFocus()) {
                                spinnerField.setText(placeholder);
                                spinnerField.setForeground(placeholderColor);
                                showingPlaceholder = true;
                            } else if (intValue != 0) {
                                showingPlaceholder = false;
                                spinnerField.setForeground(Color.BLACK);
                            }
                        }
                    });
                }
            });
        }
    }
    
    private void checkAndRemovePlaceholder() {
        String currentText = spinnerField.getText();
        if (showingPlaceholder && !currentText.equals(placeholder) && !currentText.isEmpty() && !currentText.equals("0")) {
            SwingUtilities.invokeLater(() -> {
                spinnerField.setForeground(Color.BLACK);
                showingPlaceholder = false;
            });
        }
    }
    
    private void checkAndRestorePlaceholder() {
        String currentText = spinnerField.getText();
        if (!showingPlaceholder && (currentText.isEmpty() || currentText.trim().isEmpty() || currentText.equals("0"))) {
            SwingUtilities.invokeLater(() -> {
                if (!spinnerField.hasFocus()) {
                    Object value = getValue();
                    if (value != null && value instanceof Number) {
                        int intValue = ((Number) value).intValue();
                        if (intValue == 0) {
                            spinnerField.setText(placeholder);
                            spinnerField.setForeground(placeholderColor);
                            showingPlaceholder = true;
                        }
                    }
                }
            });
        }
    }
    
    public int getIntValue() {
        if (showingPlaceholder) {
            return 0;
        }
        Object value = getValue();
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
    
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (showingPlaceholder && spinnerField != null) {
            spinnerField.setText(placeholder);
        }
    }
    
    public boolean isShowingPlaceholder() {
        return showingPlaceholder;
    }
}

