package org.hl7.fhir.r4.validation;

import java.awt.EventQueue;
import java.io.IOException;

public class GraphicalValidator {

  public ValidatorFrame frame;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          GraphicalValidator window = new GraphicalValidator();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   * @throws IOException 
   */
  public GraphicalValidator() throws IOException {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   * @throws IOException 
   */
  private void initialize() throws IOException {
    frame = new ValidatorFrame();
  }

}
