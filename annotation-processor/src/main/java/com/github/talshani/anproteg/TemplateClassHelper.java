package com.github.talshani.anproteg;

import autovalue.shaded.com.google.common.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tal Shani
 */
final class TemplateClassHelper {
    private final ExecutableElement method;
    private final Types types;

    public TemplateClassHelper(ExecutableElement method, Types types) {
        this.method = method;
        this.types = types;
    }

    public String getClassName() {
        return getClassNamePrefix() + "_" + method.getSimpleName();
    }

    public String getClassNamePrefix() {
        List<String> names = new ArrayList<String>();
        Element el = method.getEnclosingElement();
        while (el.getKind() == ElementKind.CLASS || el.getKind() == ElementKind.INTERFACE || el.getKind() == ElementKind.ENUM) {
            names.add(0, el.getSimpleName().toString());
            el = el.getEnclosingElement();
        }
        return "PrecompiledTemplate_" + Joiner.on("_").join(names);
    }

    public String getFullyQualifiedClassName() {
        return getPackageName() + "." + getClassName();
    }

    public String getPackageName() {
        return MoreElements.getPackage(method).getQualifiedName().toString();
    }

    public String getTemplate() {
        PrecompiledTemplate annotation = method.getAnnotation(PrecompiledTemplate.class);
        return annotation.value();
    }

    public ImmutableList<VariableElement> getRawParams() {
        ImmutableList.Builder<VariableElement> builder = ImmutableList.builder();
        boolean first = true;
        for (VariableElement var : method.getParameters()) {
            if (first && ignoreFirstParam()) {
                // skip
            } else {
                builder.add(var);
            }
            first = false;
        }
        return builder.build();
    }

    public String getParamsSignature() {
        StringBuilder sb = new StringBuilder();
        for (VariableElement var : getRawParams()) {
            if(sb.length() > 0) sb.append(", ");
            sb.append(var.asType().toString() + " " + var.getSimpleName().toString());
        }
        return sb.toString();
    }

    private boolean ignoreFirstParam() {
        if (method.getParameters().size() == 0) return false;
        VariableElement param = method.getParameters().get(0);
        TypeElement el = MoreTypes.asTypeElement(types, param.asType());
        return isAppendable(el);
    }

    private boolean isAppendable(TypeElement el) {
        if (el.getQualifiedName().contentEquals("java.lang.Object")) return false;
        for (TypeMirror typeMirror : el.getInterfaces()) {
            TypeElement inter = MoreTypes.asTypeElement(types, typeMirror);
            if (inter.getQualifiedName().contentEquals("java.lang.Appendable")) {
                return true;
            }
        }
        return isAppendable(MoreTypes.asTypeElement(types, el.getSuperclass()));
    }

    public String getParamNamesSignature() {
        StringBuilder sb = new StringBuilder();
        for (VariableElement var : getRawParams()) {
            if(sb.length() > 0) sb.append(", ");
            sb.append(var.getSimpleName().toString());
        }
        return sb.toString();
    }


}
