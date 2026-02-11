package com.pichincha.devpulse.ui;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.pichincha.devpulse.model.Message;
import com.pichincha.devpulse.model.MessageType;
import com.pichincha.devpulse.service.MessageService;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class DevPulseToolWindow {
  private final Project project;
  private final JPanel mainPanel;
  private final DefaultListModel<Message> listModel;
  private final JBList<Message> messageList;
  private final JPanel detailPanel;

  public DevPulseToolWindow(Project project) {
    this.project = project;
    this.mainPanel = new JPanel(new BorderLayout());
    this.listModel = new DefaultListModel<>();
    this.messageList = new JBList<>(listModel);
    this.detailPanel = new JPanel(new BorderLayout());

    initializeUI();
    loadMessages();
  }

  private void initializeUI() {
    mainPanel.setBorder(JBUI.Borders.empty(10));

    JPanel headerPanel = createHeaderPanel();
    mainPanel.add(headerPanel, BorderLayout.NORTH);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setLeftComponent(createMessageListPanel());
    splitPane.setRightComponent(createDetailPanel());
    splitPane.setDividerLocation(300);
    
    mainPanel.add(splitPane, BorderLayout.CENTER);

    messageList.setCellRenderer(new MessageListCellRenderer());
    messageList.addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        Message selected = messageList.getSelectedValue();
        if (selected != null) {
          showMessageDetail(selected);
          MessageService.getInstance().markAsRead(selected.getId());
          messageList.repaint();
        }
      }
    });
  }

  private JPanel createHeaderPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(JBUI.Borders.empty(0, 0, 10, 0));

    JBLabel titleLabel = new JBLabel("DevPulse - Centro de Mensajes");
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
    panel.add(titleLabel, BorderLayout.WEST);

    JButton refreshButton = new JButton("Actualizar");
    refreshButton.addActionListener(e -> refreshMessages());
    panel.add(refreshButton, BorderLayout.EAST);

    return panel;
  }

  private JPanel createMessageListPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(JBUI.Borders.empty(0, 0, 0, 5));

    JBLabel listTitle = new JBLabel("Mensajes");
    listTitle.setFont(listTitle.getFont().deriveFont(Font.BOLD));
    listTitle.setBorder(JBUI.Borders.empty(0, 0, 5, 0));
    panel.add(listTitle, BorderLayout.NORTH);

    JBScrollPane scrollPane = new JBScrollPane(messageList);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createDetailPanel() {
    detailPanel.setBorder(JBUI.Borders.empty(0, 5, 0, 0));
    
    JBLabel emptyLabel = new JBLabel("Selecciona un mensaje para ver los detalles");
    emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
    detailPanel.add(emptyLabel, BorderLayout.CENTER);

    return detailPanel;
  }

  private void showMessageDetail(Message message) {
    detailPanel.removeAll();
    
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBorder(JBUI.Borders.empty(10));

    JBLabel titleLabel = new JBLabel(message.getTitle());
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    contentPanel.add(titleLabel);
    contentPanel.add(Box.createVerticalStrut(10));

    JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    metaPanel.add(new JBLabel("Tipo: " + message.getType().getDisplayName()));
    metaPanel.add(new JBLabel("Prioridad: " + message.getPriority().getDisplayName()));
    if (message.getCreatedAt() != null) {
      metaPanel.add(new JBLabel("Fecha: " + message.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
    }
    contentPanel.add(metaPanel);
    contentPanel.add(Box.createVerticalStrut(15));

    JTextArea contentArea = new JTextArea(message.getContent());
    contentArea.setWrapStyleWord(true);
    contentArea.setLineWrap(true);
    contentArea.setEditable(false);
    contentArea.setOpaque(false);
    contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
    contentPanel.add(contentArea);
    contentPanel.add(Box.createVerticalStrut(15));

    if (message.getCodeExample() != null && !message.getCodeExample().isEmpty()) {
      JBLabel codeLabel = new JBLabel("Ejemplo de codigo:");
      codeLabel.setFont(codeLabel.getFont().deriveFont(Font.BOLD));
      codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      contentPanel.add(codeLabel);
      contentPanel.add(Box.createVerticalStrut(5));

      JTextArea codeArea = new JTextArea(message.getCodeExample());
      codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
      codeArea.setEditable(false);
      codeArea.setBackground(new Color(240, 240, 240));
      codeArea.setBorder(JBUI.Borders.empty(10));
      codeArea.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JBScrollPane codeScroll = new JBScrollPane(codeArea);
      codeScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
      codeScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
      contentPanel.add(codeScroll);
      contentPanel.add(Box.createVerticalStrut(15));
    }

    if (message.getLink() != null && !message.getLink().isEmpty()) {
      JButton linkButton = new JButton("Ver documentacion completa");
      linkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
      linkButton.addActionListener(e -> BrowserUtil.browse(message.getLink()));
      contentPanel.add(linkButton);
    }

    JBScrollPane scrollPane = new JBScrollPane(contentPanel);
    detailPanel.add(scrollPane, BorderLayout.CENTER);
    
    detailPanel.revalidate();
    detailPanel.repaint();
  }

  private void loadMessages() {
    listModel.clear();
    MessageService.getInstance().getAllMessages().forEach(listModel::addElement);
  }

  private void refreshMessages() {
    MessageService.getInstance().refreshMessages();
    loadMessages();
  }

  public JComponent getContent() {
    return mainPanel;
  }

  private static class MessageListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                  boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      
      if (value instanceof Message message) {
        String icon = getIconForType(message.getType());
        String readIndicator = message.isRead() ? "" : "[NEW] ";
        label.setText(String.format("<html><b>%s%s %s</b><br/><small>%s</small></html>", 
            readIndicator, icon, message.getTitle(), message.getType().getDisplayName()));
        
        if (!message.isRead()) {
          label.setFont(label.getFont().deriveFont(Font.BOLD));
        }
      }
      
      return label;
    }

    private String getIconForType(MessageType type) {
      return switch (type) {
        case ALERT -> "[!]";
        case GUIDELINE -> "[G]";
        case DOCUMENTATION -> "[D]";
        case ANNOUNCEMENT -> "[A]";
        case TRAINING_PILL -> "[T]";
        default -> "[i]";
      };
    }
  }
}
