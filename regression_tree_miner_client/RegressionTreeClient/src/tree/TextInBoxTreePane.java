package tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

public class TextInBoxTreePane extends JComponent{
	
	private final TreeLayout<NodeInfo> treeLayout;
	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.ORANGE;
	private final static Color BORDER_COLOR = Color.DARK_GRAY;
	private final static Color TEXT_COLOR = Color.BLACK;
	
	public TextInBoxTreePane(TreeLayout<NodeInfo> treeLayout){
		this.treeLayout = treeLayout;
		Dimension size = treeLayout.getBounds().getBounds().getSize();
		setPreferredSize(size);
	}
	
	private TreeForTreeLayout<NodeInfo> getTree(){
		return treeLayout.getTree();
	}
	
	private Iterable<NodeInfo> getChildren(NodeInfo parent){
		return getTree().getChildren(parent);
	}
	
	private Rectangle2D.Double getBoundsOfNode(NodeInfo node){
		return treeLayout.getNodeBounds().get(node);
	}
	
	private void paintEdges(Graphics g , NodeInfo parent){
		if(!getTree().isLeaf(parent)){
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for(NodeInfo child : getChildren(parent)){
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				g.drawLine((int) x1 , (int) y1, (int) b2.getCenterX(), (int) b2.getCenterY());
				paintEdges(g,child);
			}
		}
	}
	
	private void paintBox(Graphics g, NodeInfo textInBox) {
		// draw the box in the background
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		g.setColor(BORDER_COLOR);
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String[] lines = textInBox.getLabel().split("\n");
		FontMetrics m = getFontMetrics(getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + m.getAscent() + m.getLeading() + 1;
		for (int i = 0; i < lines.length; i++) {
			g.drawString(lines[i], x, y);
			y += m.getHeight();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		paintEdges(g, getTree().getRoot());

		// paint the boxes
		for (NodeInfo textInBox : treeLayout.getNodeBounds().keySet()) {
			paintBox(g, textInBox);
		}
	}

}
