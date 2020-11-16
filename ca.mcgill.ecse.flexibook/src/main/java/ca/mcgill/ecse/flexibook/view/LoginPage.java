package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class LoginPage extends JFrame {
  private static final long serialVersionUID = 1307717424939065361L;
  private JLabel loginLabel;

  public LoginPage() {
      initComponents();
  }
  private void initComponents(){
    loginLabel = new JLabel();
    loginLabel.setText("Login Tab here");

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Login Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(loginLabel)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(loginLabel)
      )
    );
    pack();
  }
}
