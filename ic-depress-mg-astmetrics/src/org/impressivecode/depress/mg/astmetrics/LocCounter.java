/*
ImpressiveCode Depress Framework
Copyright (C) 2013  ImpressiveCode contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.mg.astmetrics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class LocCounter {
    private static Map<String, Integer> methods;

    public static Map<String, Integer> countLocForFile(File file) throws IOException {
        methods = new HashMap<String, Integer>();

        ASTParser parser = ASTParser.newParser(AST.JLS4);
        String[] classpathEntries = new String[] { "." };
        String[] sourcepathEntries = new String[] { file.getParent() };
        parser.setEnvironment(classpathEntries, sourcepathEntries, null, false);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        String fileContents = FileUtils.readFileToString(file);
        parser.setSource(fileContents.toCharArray());

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        cu.accept(new ASTVisitor() {
            private Stack<char[]> fQualifiers = new Stack<char[]>();

            public boolean visit(PackageDeclaration node) {
                fQualifiers.push(node.getName().getFullyQualifiedName().toCharArray());
                fQualifiers.push(".".toCharArray());
                return true;
            }

            public void endVisit(PackageDeclaration node) {
            }

            public boolean visit(TypeDeclaration node) {
                fQualifiers.push(node.getName().getIdentifier().toCharArray());
                fQualifiers.push(".".toCharArray());
                return true;
            }

            public void endVisit(TypeDeclaration node) {
                fQualifiers.pop();
                fQualifiers.pop();
            }

            public boolean visit(MethodDeclaration node) {
                int size = -1;
                if (node.getBody() == null) {
                    size = 0;
                } else {
                    String body = node.getBody().toString().trim();
                    if ("{\n}".equals(body)) {
                        size = 0;
                    } else {
                        size = StringUtils.countMatches(body, "\n") - 1;
                    }
                }

                methods.put(getMethodSignature(node), size);

                return true;
            }

            private String getMethodSignature(MethodDeclaration node) {
                StringBuffer methodName = new StringBuffer();

                methodName.append(node.getName());
                methodName.append("(");

                for (Object parameter : node.parameters()) {
                    VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;

                    String type = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY)
                            .toString();

                    methodName.append(type);

                    for (int i = 0; i < variableDeclaration.getExtraDimensions(); i++) {
                        methodName.append("[]");
                    }
                    methodName.append(",");
                }

                if (node.parameters().size() > 0) {
                    methodName.deleteCharAt(methodName.length() - 1);
                }
                methodName.append(")");

                return getCurrentClassName() + methodName.toString();
            }

            private String getCurrentClassName() {
                StringBuffer className = new StringBuffer();

                Stack<char[]> fQualifiers1 = fQualifiers;

                for (Iterator<char[]> iterator = fQualifiers1.iterator(); iterator.hasNext();) {
                    char[] cs = (char[]) iterator.next();
                    className.append(cs);
                }

                return className.toString();
            }
        });

        return methods;
    }
}
