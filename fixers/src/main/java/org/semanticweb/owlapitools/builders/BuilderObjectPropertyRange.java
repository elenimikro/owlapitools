package org.semanticweb.owlapitools.builders;

import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;

/** Builder class for OWLObjectPropertyRangeAxiom */
public class BuilderObjectPropertyRange extends
        BaseObjectBuilder<OWLObjectPropertyRangeAxiom, BuilderObjectPropertyRange> {
    /** builder initialized from an existing object
     * 
     * @param expected
     *            the existing object */
    public BuilderObjectPropertyRange(OWLObjectPropertyRangeAxiom expected) {
        withProperty(expected.getProperty()).withRange(expected.getRange())
                .withAnnotations(expected.getAnnotations());
    }

    /** uninitialized builder */
    public BuilderObjectPropertyRange() {}

    @Override
    public OWLObjectPropertyRangeAxiom buildObject() {
        return df.getOWLObjectPropertyRangeAxiom(property, range, annotations);
    }
}
