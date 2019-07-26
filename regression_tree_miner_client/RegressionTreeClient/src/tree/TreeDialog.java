package tree;

import javax.swing.JDialog;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

public class TreeDialog extends JDialog {
	
	public TreeDialog(DefaultTreeForTreeLayout<NodeInfo> tree){
		double gapBetweenLevels = 40;
		double gapBetweenNodes = 40;
		DefaultConfiguration<NodeInfo> configuration = new DefaultConfiguration<NodeInfo>(gapBetweenLevels , gapBetweenNodes);
		NodeInfoExtentProvider nodeInfoExtentProvider = new NodeInfoExtentProvider();
		TreeLayout<NodeInfo> treeLayout = new TreeLayout<NodeInfo>(tree , nodeInfoExtentProvider , configuration);
		TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
