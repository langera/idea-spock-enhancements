package com.cholick.idea.spock.highlight;

import com.cholick.idea.spock.GrLabeledStatementAdapter;
import com.cholick.idea.spock.HighlightInfoFactory;
import com.cholick.idea.spock.config.SpockConfig;
import com.cholick.idea.spock.data.SpockLabel;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement;

import java.awt.Font;

public class SpockPsiElementVisitor extends PsiElementVisitor {

    private HighlightInfoHolder highlightInfoHolder;
    private TextAttributes textAttributes;

    @Override
    public void visitElement(PsiElement element) {
        if (!highlightInfoHolder.hasErrorResults()) {
            if (element instanceof GrLabeledStatement) {
                GrLabeledStatement labelElement = (GrLabeledStatement) element;
                if (SpockLabel.contains(labelElement.getName())) {
                    PsiElement label = GrLabeledStatementAdapter.getInstance().getLabel(labelElement);
                    highlightInfoHolder.add(createHighlightInfo(label));
                }
            }
        }
    }

    private HighlightInfo createHighlightInfo(@NotNull PsiElement element) {
        return HighlightInfoFactory.getInstance().createHighlightInfo(SpockLabelHighlightInfoTypes.SPOCK_LABEL, element, null, buildTextAttributes());
    }

    private TextAttributes buildTextAttributes() {
        if (textAttributes == null) {
            SpockConfig spockConfig = SpockConfig.getInstance();
            int fontStyle = Font.PLAIN;
            if (spockConfig.labelBold) {
                fontStyle = fontStyle | Font.BOLD;
            }
            if (spockConfig.labelItalics) {
                fontStyle = fontStyle | Font.ITALIC;
            }
            textAttributes = new TextAttributes(spockConfig.labelColor, null, null, EffectType.BOXED, fontStyle);
        }
        return textAttributes;
    }

    public void clear() {
        highlightInfoHolder = null;
        textAttributes = null;
    }

    public void setHighlightInfoHolder(HighlightInfoHolder highlightInfoHolder) {
        this.highlightInfoHolder = highlightInfoHolder;
    }

}
