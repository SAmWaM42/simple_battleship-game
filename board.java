import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.*;

class board implements ActionListener   {
    JFrame screen=new JFrame("Battleship");
    final static int max_tiles = 100;
    // board pieces for each player
    tile tiles[] = new tile[max_tiles];
    player players[] = new player[2];
    // player boards
    JPanel panes = new JPanel();
    JPanel info_pane = new JPanel();
    // overlay panel
    JPanel overlay = new JPanel();
    int playing = 0;
    int board_state[] = new int[2];
    int states[][] = new int[2][max_tiles];
    JLabel score[] = new JLabel[2];
    JLabel final_outcome;
    JLabel playLabel = new JLabel("");
    int player_num;

    public board() {

       
       

        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setLayout(new GridLayout(1, 1));
        overlay.setLayout(new GridLayout(1, 2));
        GridBagConstraints pos = new GridBagConstraints();
        int player_num = 2;
        // initializing the tiles
        for (int j = 0; j < player_num; j++) {
            for (int i = 0; i < max_tiles; i++) {

                states[j][i] = 0;
            }

        }
        for (int i = 0; i < max_tiles; i++) {

            tiles[i] = new tile();
            tiles[i].button = new JButton();
            tiles[i].button.setSize(30, 30);
            tiles[i].button.setBackground(Color.BLACK);
            tiles[i].button.addActionListener(this);
        }
        // setting the individual player boards

        for (int i = 0; i < player_num; i++) {

            score[i] = new JLabel("");
            players[i] = new player();
            players[i].pieces = 7;
            board_state[i] = 0;
            final_outcome = new JLabel("");
            panes = new JPanel();
            screen.addKeyListener(
                new KeyListener() {
                    public void keyPressed(java.awt.event.KeyEvent e) {
                        char key = e.getKeyChar();
                        if (key == 's') {
                            int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
                            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
                            int close = 0;
                            for (int i = 0; i < max_tiles; i++) {
        
                                if (tiles[i].button.getX() >= x + 60 && tiles[i].button.getX() >= x - 60) {
                                    if (tiles[i].button.getY() >= y + 60
                                            && tiles[i].button.getY() >= y - 60) {
                                        close++;
                                    }
        
                                }
                            }
                            final_outcome.setText(String.valueOf(close));
        
                        }
        
                    }
        
                    public void keyReleased(java.awt.event.KeyEvent e) {
                    };
        
                    public void keyTyped(java.awt.event.KeyEvent e) {
                    };
        
                });
            panes.setLayout(new GridLayout(10, 10, 2, 2));
            for (int j = 0; j < max_tiles; j++) {

                panes.add(tiles[j].button);
            }

            panes.setVisible(true);

        }
        // info for who is playing and the scores
        info_pane.setLayout(new GridLayout(1, 4, 2, 2));
        score[0].setText("0");
        score[0].setFont(new Font("Ariel", 0, 30));
        info_pane.add(score[0]);
        score[1].setText("0");
        score[1].setFont(new Font("Ariel", 0, 30));
        info_pane.add(score[1]);
        playLabel.setText("player 1");
        playLabel.setFont(new Font("Ariel", 0, 30));
        info_pane.add(playLabel);
        info_pane.setSize((int) overlay.getSize().getWidth(), 150);
        final_outcome.setText(" ");
        final_outcome.setFont(new Font("Ariel", 0, 30));
        info_pane.add(final_outcome);

        // adding all the panes
        pos.gridx = 1;
        pos.gridy = 1;
        pos.gridwidth = (int) overlay.getSize().getWidth();
        pos.insets.bottom = 20;
        overlay.add(info_pane, pos);
        pos.gridx = 1;
        pos.gridy = 2;
        pos.insets.bottom = 5;
        pos.gridheight = 1000;
        overlay.add(panes, pos);
        pos.gridx = 1;
        overlay.setSize(screen.getSize());
        screen.add(overlay);
        
        screen.setSize(600, 600);
     
    }
 
    

    public void change_turn() {

        playing = (playing + 1) % 2;
        for (int i = 0; i < max_tiles; i++) {
            tiles[i].state = states[playing][i];
        }
        playLabel.setText("player".concat(String.valueOf(playing + 1)));

    }

    public String game_outcome(int states[]) {
        String outcome = "";
        for (int i = 0; i <max_tiles; i++) {
            if (states[i] == 1) {
                outcome = "lose";
            }
        }
        if (outcome == "") {
            outcome = "win";
        }
        return outcome;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        for (int i = 0; i < max_tiles; i++) {
            if (source == tiles[i].button) {
                if (board_state[playing] == 0 && players[playing].pieces != 0) {

                    tiles[i].state = 1;
                    states[playing][i] = 1;
                    players[playing].pieces -= 1;
                    tiles[i].button.setBackground(Color.green);
                    if (players[playing].pieces == 0) {
                        board_state[playing] = 1;

                        for (int j = 0; j < max_tiles; j++) {
                            tiles[j].button.setBackground(Color.black);
                        }
                        change_turn();
                    }

                }
                if (board_state[playing] == 1) {
                    if (source == tiles[i].button) {
                        if (tiles[i].state == 1) {
                            final_outcome.setText("hit");
                            players[playing].score += 10;
                            states[playing][i] = 2;
                            if (states[playing][i] == 2) {
                                tiles[i].button.setBackground(Color.RED);
                                tiles[i].button.removeActionListener(this);
                            }
                            score[playing].setText(String.valueOf(players[playing].score));
                            change_turn();
                        }
                         else {
                            players[playing].write = "miss";
                            final_outcome.setText(players[playing].write);
                            change_turn();
                        }
                        if (game_outcome(states[playing]) == "win") {

                            final_outcome.setText("player".concat(String.valueOf(playing + 1).concat("wins")));

                        }

                    }

                }

            }
            if (tiles[i].state == 2) 
            {
                if(playing==0)
               { tiles[i].button.setBackground(Color.red);}
               else
               {
                tiles[i].button.setBackground(Color.magenta);
               }
            }
        }

              
    }

    

    public static void main(String[] args) {
     board see=new board();
      
       
    }
}