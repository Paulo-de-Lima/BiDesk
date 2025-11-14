package com.bidesk.view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlaceholderTextField extends JTextField {
    private String placeholder;
    private Color placeholderColor = new Color(150, 150, 150);
    private boolean showingPlaceholder;

    public PlaceholderTextField(String placeholder) {
        super();
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        setText(placeholder);
        setForeground(placeholderColor);
        
        // Listener para teclas pressionadas - remove placeholder quando começa a digitar
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (showingPlaceholder) {
                    char keyChar = e.getKeyChar();
                    // Se não for backspace ou delete, remove o placeholder
                    if (keyChar != KeyEvent.VK_BACK_SPACE && keyChar != KeyEvent.VK_DELETE && keyChar != KeyEvent.CHAR_UNDEFINED) {
                        setText("");
                        setForeground(Color.BLACK);
                        showingPlaceholder = false;
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Listener para quando o campo perde o foco
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Placeholder continua visível - não faz nada
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = PlaceholderTextField.super.getText().trim();
                if (text.isEmpty()) {
                    setText(placeholder);
                    setForeground(placeholderColor);
                    showingPlaceholder = true;
                }
            }
        });
        
        // Listener para mudanças no documento (backup)
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (showingPlaceholder) {
                    String text = PlaceholderTextField.super.getText();
                    if (!text.equals(placeholder) && !text.isEmpty()) {
                        SwingUtilities.invokeLater(() -> {
                            if (text.startsWith(placeholder)) {
                                setText(text.substring(placeholder.length()));
                            }
                            setForeground(Color.BLACK);
                            showingPlaceholder = false;
                        });
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!showingPlaceholder) {
                    String text = PlaceholderTextField.super.getText().trim();
                    if (text.isEmpty() && !hasFocus()) {
                        SwingUtilities.invokeLater(() -> {
                            setText(placeholder);
                            setForeground(placeholderColor);
                            showingPlaceholder = true;
                        });
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    @Override
    public String getText() {
        if (showingPlaceholder) {
            return "";
        }
        String text = super.getText();
        if (text.equals(placeholder)) {
            return "";
        }
        return text;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (showingPlaceholder) {
            setText(placeholder);
        }
    }

    public boolean isShowingPlaceholder() {
        return showingPlaceholder;
    }
}

