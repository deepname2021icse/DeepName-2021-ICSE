package edu.utd.fse19.data.clean;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.StringLiteral;;

public class AstVisitor extends ASTVisitor {
	@Override
	public boolean visit(final InfixExpression node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		node.delete();
		return super.visit(node);
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		node.delete();
		return super.visit(node);
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		node.delete();
		return super.visit(node);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
//		node.delete();
//		node.setLiteralValue("LIT(String)");
		return super.visit(node);
	}
	
	@Override
	public boolean visit(BlockComment node) {
		node.delete();
		return super.visit(node);
	}
	@Override
	public boolean visit(LineComment node) {
		node.delete();
		return super.visit(node);
	}
}
