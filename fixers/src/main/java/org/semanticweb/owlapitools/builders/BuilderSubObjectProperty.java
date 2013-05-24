package org.semanticweb.owlapitools.builders;

import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

/** Builder class for OWLSubObjectPropertyOfAxiom */
public class BuilderSubObjectProperty
        extends
        BaseSubBuilder<OWLSubObjectPropertyOfAxiom, BuilderSubObjectProperty, OWLObjectPropertyExpression> {
    /** builder initialized from an existing object
     * 
     * @param expected
     *            the existing object */
    public BuilderSubObjectProperty(OWLSubObjectPropertyOfAxiom expected) {
        withSub(expected.getSubProperty()).withSup(expected.getSuperProperty())
                .withAnnotations(expected.getAnnotations());
    }

    /** uninitialized builder */
    public BuilderSubObjectProperty() {}

    @Override
    public OWLSubObjectPropertyOfAxiom buildObject() {
        return df.getOWLSubObjectPropertyOfAxiom(sub, sup, annotations);
    }
}
