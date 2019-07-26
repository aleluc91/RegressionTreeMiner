package tree;

import org.abego.treelayout.NodeExtentProvider;

class NodeInfoExtentProvider implements NodeExtentProvider<NodeInfo>{

	@Override
	public double getHeight(NodeInfo node) {
		return node.getHeight();
	}

	@Override
	public double getWidth(NodeInfo node) {
		return node.getWidth();
	}

}
