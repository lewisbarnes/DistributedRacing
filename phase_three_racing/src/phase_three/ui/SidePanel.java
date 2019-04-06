package phase_three.ui;
import java.awt.Dimension;
import java.awt.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import phase_three.control.GameController;
import phase_three.entity.Player;
/**
 * 
 * @author LBARNES
 * 
 */
public class SidePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Minimap m_minimap;
    private JLabel m_lapCountLabel;
    private JLabel m_nearestTileLabel;
    JList<Player> playerList;
    public SidePanel() {
        m_lapCountLabel = new JLabel(getLapCountLabelText());
        add(m_lapCountLabel);
        m_nearestTileLabel = new JLabel(getPlayerNearestTileString());
        add(m_nearestTileLabel);
        m_minimap = new Minimap();
        add(m_minimap);
        playerList = getPlayerListPanel();
        playerList.setPreferredSize(new Dimension((int) m_minimap.getPreferredSize().getWidth()- 50, 250));
        playerList.setFocusable(false);
        add(playerList);
        setPreferredSize(new Dimension((int) 
                m_minimap.getPreferredSize().getWidth(), 650));
        m_minimap.repaint();
        setVisible(true);
    }
	
    private JList<Player> getPlayerListPanel() {
        DefaultListModel<Player> listModel = new DefaultListModel<Player>();
        listModel.addElement(GameController.getInstance().getLocalPlayer());
        for(Player p : GameController.getInstance().getPlayers()) {
            listModel.addElement(p);
        }
        JList<Player> playerList = new JList<Player>(listModel);
        return playerList;
    }
    
    private void updatePlayerList() {
    	DefaultListModel<Player> listModel = new DefaultListModel<Player>();
        listModel.addElement(GameController.getInstance().getLocalPlayer());
        for(Player p : GameController.getInstance().getPlayers()) {
            listModel.addElement(p);
        }
        playerList.setModel(listModel);
    }
	
    /**
     * Updates the lap count label and repaints the minimap
     * @see Minimap
     */
    public void update() {
        m_lapCountLabel.setText(getLapCountLabelText());
        m_nearestTileLabel.setText(getPlayerNearestTileString());
        updatePlayerList();
        m_minimap.repaint();
    }
	
    /**
     * Returns a string used to set the lap count label text
     * @return the string containing the current lap and total lap count
     */
    private String getLapCountLabelText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Lap ");
        stringBuilder.append(GameController.getInstance().
                getLocalPlayer().getCurrentLap());
        stringBuilder.append(" of ");
        stringBuilder.append(GameController.getInstance().
                getTotalLapCount());
        return stringBuilder.toString();
    }
    
    private String getPlayerNearestTileString() {
    	int nearestX = ((int)GameController.getInstance().getLocalPlayer().getXPos()+49)/50 * 50;
    	int nearestY = ((int)GameController.getInstance().getLocalPlayer().getYPos()+49)/50 * 50;
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append("Nearest Tile: ");
    	stringBuilder.append(nearestX);
    	stringBuilder.append(", ");
    	stringBuilder.append(nearestY);
    	return stringBuilder.toString();
    }
}