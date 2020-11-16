package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class BusinessInfoPage extends JFrame {
  private static final long serialVersionUID = -941637358529064014L;
  
  private JLabel businessInfoLabel;
  public BusinessInfoPage() {
    initComponents();
  }
  private void initComponents(){
    businessInfoLabel = new JLabel();
    businessInfoLabel.setText("Business Info Tab here");

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Business Info Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(businessInfoLabel)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(businessInfoLabel)
      )
    );
    pack();
  }
}
