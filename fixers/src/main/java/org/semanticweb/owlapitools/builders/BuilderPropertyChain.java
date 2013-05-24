package org.semanticweb.owlapitools.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

/** Builder class for OWLSubPropertyChainOfAxiom */
public class BuilderPropertyChain extends
        BaseObjectPropertyBuilder<OWLSubPropertyChainOfAxiom, BuilderPropertyChain> {
    private List<OWLObjectPropertyExpression> chain = new ArrayList<OWLObjectPropertyExpression>();

    /** builder initialized from an existing object
     * 
     * @param expected
     *            the existing object */
    public BuilderPropertyChain(OWLSubPropertyChainOfAxiom expected) {
        withPropertiesInChain(expected.getPropertyChain()).withProperty(
                expected.getSuperProperty()).withAnnotations(expected.getAnnotations());
    }

    /** uninitialized builder */
    public BuilderPropertyChain() {}

    /** @param arg
     *            property
     * @return builder */
    public BuilderPropertyChain withPropertyInChain(OWLObjectPropertyExpression arg) {
        chain.add(arg);
        return this;
    }

    /** @param arg
     *            properties
     * @return builder */
    public BuilderPropertyChain withPropertiesInChain(
            Collection<OWLObjectPropertyExpression> arg) {
        chain.addAll(arg);
        return this;
    }

    @Override
    public OWLSubPropertyChainOfAxiom buildObject() {
        return df.getOWLSubPropertyChainOfAxiom(chain, property, annotations);
    }
}
