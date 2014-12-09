/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version 
 *******************************************************************************/

package ilg.gnuarmeclipse.debug.gdbjtag.datamodel;

import ilg.gnuarmeclipse.core.StringUtils;
import ilg.gnuarmeclipse.core.Xml;
import ilg.gnuarmeclipse.packs.core.tree.Leaf;

public class SvdObjectDMNode {

	// ------------------------------------------------------------------------

	/**
	 * Reference to the original node in the generic tree parsed from SVD.
	 */
	protected Leaf fNode;

	/**
	 * Reference to the node referenced, or null.
	 */
	private Leaf fDerivedFromNode;

	/**
	 * The description string. If multiple lines, they were already joined.
	 */
	private String fDescription;

	/**
	 * The children nodes, with types depending on individual node.
	 */
	private SvdObjectDMNode[] fChildren;

	// ------------------------------------------------------------------------

	public SvdObjectDMNode(Leaf node) {

		fNode = node;
		fDerivedFromNode = null;

		fDescription = null;

		fChildren = null;
	}

	public void dispose() {

		if (fNode == null) {
			return; // Already disposed
		}

		// System.out.println("Dispose " + this);
		fNode = null;
		fDerivedFromNode = null;

		fDescription = null;

		if (fChildren != null) {

			// Dispose children
			for (int i = 0; i < fChildren.length; ++i) {
				fChildren[i].dispose();
			}

			fChildren = null;
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the original SVD node, in the generic tree.
	 * 
	 * @return a generic tree node.
	 */
	public Leaf getNode() {
		return fNode;
	}

	/**
	 * Get name. Mandatory, cannot be derived.
	 * 
	 * @return a short (usually upper case) string.
	 */
	public String getName() {
		return fNode.getProperty("name");
	}

	/**
	 * Check if the node is derived from another node.
	 * 
	 * @return true if derived.
	 */
	public boolean isDerived() {
		return fNode.hasProperty("derivedFrom");
	}

	/**
	 * Get the node that this node is derived from.
	 * 
	 * @return the derived from node, or null if not derived or not found.
	 */
	public Leaf getDerivedFromNode() {

		if (fDerivedFromNode == null) {

			fDerivedFromNode = findDerivedFromNode();
		}

		// If not derived, return null
		return fDerivedFromNode;
	}

	protected Leaf findDerivedFromNode() {
		return null;
	}

	/**
	 * Get the default display name.
	 * 
	 * @return a string.
	 */
	public String getDisplayName() {
		return getName();
	}

	/**
	 * Get field description. In case the description spans multiple lines,
	 * these lines are joined together in a single line, with each of the
	 * individual lines trimmed.
	 * <p>
	 * If not present, the derived from node description is returned.
	 * 
	 * @return a string with the field description, possibly empty.
	 */
	public String getDescription() {

		if (fDescription == null) {

			fDescription = fNode.getDescription();
			if ((fDescription.isEmpty()) && (getDerivedFromNode() != null)) {
				fDescription = getDerivedFromNode().getDescription();
			}
			if (fDescription != null) {
				// Process multiple lines descriptions
				fDescription = Xml.joinMultiLine(fDescription);
			} else {
				fDescription = "";
			}

			fDescription = StringUtils.capitalizeFirst(fDescription);
		}
		return fDescription;
	}

	/**
	 * Get the cluster, register, field children nodes.
	 * 
	 * @return an array of nodes.
	 */
	public SvdObjectDMNode[] getChildren() {

		if (fChildren == null) {

			// If any, try to use the derivedFrom node children.
			fChildren = prepareChildren(getDerivedFromNode());

			if (fChildren == null) {
				// Try to get the children from the current node.
				fChildren = prepareChildren(fNode);
			}

			if (fChildren == null) {
				// If none worked, return an empty array.
				fChildren = new SvdDMNode[0];
			}
		}

		return fChildren;
	}

	/**
	 * To be redefined by nodes that have children.
	 * 
	 * @return an array of nodes or null if no children.
	 */
	protected SvdObjectDMNode[] prepareChildren(Leaf node) {
		return null;
	}

	// ------------------------------------------------------------------------
}