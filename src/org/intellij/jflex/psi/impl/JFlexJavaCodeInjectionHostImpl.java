/*
 * Copyright 2011-present Greg Shrago
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.jflex.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.tree.IElementType;
import org.intellij.jflex.psi.JFlexPsiElementFactory;
import org.jetbrains.annotations.NotNull;


public class JFlexJavaCodeInjectionHostImpl extends JFlexCompositeImpl implements PsiLanguageInjectionHost {

  public JFlexJavaCodeInjectionHostImpl(IElementType type) {
    super(type);
  }

  @Override
  public boolean isValidHost() {
    return true;
  }

  public JFlexJavaCodeInjectionHostImpl updateText(@NotNull String text) {
    PsiElement newElement = JFlexPsiElementFactory.createJavaCodeFromText(getProject(), text);
    return (JFlexJavaCodeInjectionHostImpl)this.replace(newElement);
  }

  @NotNull
  public LiteralTextEscaper<JFlexJavaCodeInjectionHostImpl> createLiteralTextEscaper() {
    return new LiteralTextEscaper<JFlexJavaCodeInjectionHostImpl>(this) {
      public boolean decode(@NotNull TextRange textrange, @NotNull StringBuilder stringbuilder) {
        stringbuilder.append(myHost.getText(), textrange.getStartOffset(), textrange.getEndOffset());
        return true;
      }

      public int getOffsetInHost(int i, @NotNull TextRange textrange) {
        int j = i + textrange.getStartOffset();
        if (j < textrange.getStartOffset()) {
          j = textrange.getStartOffset();
        }
        if (j > textrange.getEndOffset()) {
          j = textrange.getEndOffset();
        }
        return j;
      }

      public boolean isOneLine() {
        return false;
      }
    };
  }
}
