package com.pichincha.devpulse.ui;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.pichincha.devpulse.model.Message;
import com.pichincha.devpulse.model.MessageType;
import com.pichincha.devpulse.service.MessageService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DevPulseToolWindow implements MessageService.MessageListener {
  private final Project project;
  private final JPanel mainPanel;
  private final DefaultListModel<Message> listModel;
  private final JBList<Message> messageList;
  private final JPanel detailPanel;
  private final SearchTextField searchField;

  public DevPulseToolWindow(Project project) {
    this.project = project;
    this.mainPanel = new JPanel(new BorderLayout());
    this.listModel = new DefaultListModel<>();
    this.messageList = new JBList<>(listModel);
    this.detailPanel = new JPanel(new BorderLayout());
    this.searchField = new SearchTextField();

    MessageService.getInstance().addMessageListener(this);

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

    searchField.addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent e) {
        loadMessages();
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

    JPanel topPanel = new JPanel(new BorderLayout());
    JBLabel listTitle = new JBLabel("Mensajes");
    listTitle.setFont(listTitle.getFont().deriveFont(Font.BOLD));
    listTitle.setBorder(JBUI.Borders.empty(0, 0, 5, 0));
    topPanel.add(listTitle, BorderLayout.NORTH);
    
    searchField.getTextEditor().getEmptyText().setText("Buscar...");
    topPanel.add(searchField, BorderLayout.CENTER);
    topPanel.setBorder(JBUI.Borders.emptyBottom(10));
    
    panel.add(topPanel, BorderLayout.NORTH);

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
    contentPanel.setBorder(JBUI.Borders.empty(20));

    JBLabel titleLabel = new JBLabel(message.getTitle());
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    contentPanel.add(titleLabel);
    contentPanel.add(Box.createVerticalStrut(15));

    JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    metaPanel.setOpaque(false);

    String typeIcon = getIconForType(message.getType());
    JBLabel typeLabel = new JBLabel(typeIcon + " " + message.getType().getDisplayName());
    typeLabel.setForeground(JBColor.GRAY);
    metaPanel.add(typeLabel);

    metaPanel.add(Box.createHorizontalStrut(20));

    JBLabel priorityLabel = new JBLabel("Prioridad: " + message.getPriority().getDisplayName());
    priorityLabel.setForeground(getPriorityColor(message.getPriority()));
    metaPanel.add(priorityLabel);

    if (message.getCreatedAt() != null) {
      metaPanel.add(Box.createHorizontalStrut(20));
      JBLabel dateLabel = new JBLabel(message.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
      dateLabel.setForeground(JBColor.GRAY);
      metaPanel.add(dateLabel);
    }
    contentPanel.add(metaPanel);
    contentPanel.add(Box.createVerticalStrut(20));

    JEditorPane contentPane = new JEditorPane("text/html",
        "<html><body style='font-family: sans-serif; font-size: 11pt; color: " + getHexColor(JBColor.foreground()) + "'>" +
        message.getContent().replace("\n", "<br>") + "</body></html>");
    contentPane.setEditable(false);
    contentPane.setOpaque(false);
    contentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    contentPanel.add(contentPane);
    contentPanel.add(Box.createVerticalStrut(20));

    if (message.getCodeExample() != null && !message.getCodeExample().isEmpty()) {
      JBLabel codeLabel = new JBLabel("Ejemplo de código:");
      codeLabel.setFont(codeLabel.getFont().deriveFont(Font.BOLD));
      codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      contentPanel.add(codeLabel);
      contentPanel.add(Box.createVerticalStrut(8));

      JTextArea codeArea = new JTextArea(message.getCodeExample());
      codeArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
      codeArea.setEditable(false);
      codeArea.setBackground(new JBColor(new Color(245, 245, 245), new Color(43, 43, 43)));
      codeArea.setForeground(new JBColor(new Color(0, 0, 0), new Color(169, 183, 198)));
      codeArea.setBorder(JBUI.Borders.empty(15));
      codeArea.setAlignmentX(Component.LEFT_ALIGNMENT);

      JBScrollPane codeScroll = new JBScrollPane(codeArea);
      codeScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
      codeScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
      codeScroll.setBorder(BorderFactory.createLineBorder(JBColor.border()));
      contentPanel.add(codeScroll);
      contentPanel.add(Box.createVerticalStrut(20));
    }

    if (message.getLink() != null && !message.getLink().isEmpty()) {
      JButton linkButton = new JButton("Ver documentación ↗");
      linkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
      linkButton.addActionListener(e -> BrowserUtil.browse(message.getLink()));
      contentPanel.add(linkButton);
    }

    JBScrollPane scrollPane = new JBScrollPane(contentPanel);
    scrollPane.setBorder(null);
    detailPanel.add(scrollPane, BorderLayout.CENTER);

    detailPanel.revalidate();
    detailPanel.repaint();
  }

  private Color getPriorityColor(com.pichincha.devpulse.model.Priority priority) {
    return switch (priority) {
      case CRITICAL -> JBColor.RED;
      case HIGH -> JBColor.ORANGE;
      default -> JBColor.GRAY;
    };
  }

  private String getHexColor(Color color) {
    return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
  }

  private void loadMessages() {
    String filter = searchField.getText().toLowerCase();
    listModel.clear();
    
    List<Message> allMessages = MessageService.getInstance().getAllMessages();
    List<Message> filtered = allMessages.stream()
        .filter(m -> m.getTitle().toLowerCase().contains(filter) || 
                     m.getContent().toLowerCase().contains(filter) ||
                     m.getType().getDisplayName().toLowerCase().contains(filter))
        .collect(Collectors.toList());
        
    filtered.forEach(listModel::addElement);
  }

  private void refreshMessages() {
    MessageService.getInstance().refreshMessages();
  }

  @Override
  public void onMessagesUpdated() {
    loadMessages();
  }

  public JComponent getContent() {
    return mainPanel;
  }

  private static String getIconForType(MessageType type) {
    return switch (type) {
      case ALERT -> "🚨";
      case GUIDELINE -> "📜";
      case DOCUMENTATION -> "📘";
      case ANNOUNCEMENT -> "📢";
      case TRAINING_PILL -> "💊";
      default -> "ℹ️";
    };
  }

  private static class MessageListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (value instanceof Message message) {
        String icon = getIconForType(message.getType());
        String readIndicator = message.isRead() ? "" : "🔵 ";

        label.setBorder(JBUI.Borders.empty(5, 10));
        label.setText(String.format("<html><div style='margin-bottom: 2px;'><b>%s%s %s</b></div><div style='color: #888888; font-size: 0.9em;'>%s</div></html>",
            readIndicator, icon, message.getTitle(), message.getType().getDisplayName()));

        if (!message.isRead()) {
          label.setFont(label.getFont().deriveFont(Font.BOLD));
        }
      }

      return label;
    }
  }
}