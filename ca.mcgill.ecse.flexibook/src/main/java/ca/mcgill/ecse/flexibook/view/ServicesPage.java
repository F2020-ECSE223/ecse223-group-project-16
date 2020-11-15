package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class ServicesPage extends JFrame {
  private static final long serialVersionUID = 4990227802404187714L;
  private JLabel serviceLabel;

  public ServicesPage() {
      initComponents();
  }
  private void initComponents(){
    serviceLabel = new JLabel();
    serviceLabel.setText("Services Tab here");

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Services Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(serviceLabel)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(serviceLabel)
      )
    );
    pack();
  }
}
