package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class SignUpPage extends JFrame {
  private static final long serialVersionUID = -6027711256830801450L;
  private JLabel signUpLabel;

  public SignUpPage() {
      initComponents();
  }
  private void initComponents(){
    signUpLabel = new JLabel();
    signUpLabel.setText("SignUp Tab here");

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("SignUp Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(signUpLabel)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(signUpLabel)
      )
    );
    pack();
  }
}
