package org.semanticweb.owlapitools.builders;

import org.semanticweb.owlapi.model.OWLDataMaxCardinality;

/** Builder class for OWLDataMaxCardinality */
public class BuilderDataMaxCardinality extends
        BaseDataBuilder<OWLDataMaxCardinality, BuilderDataMaxCardinality> {
    private int cardinality = -1;

    /** uninitialized builder */
    public BuilderDataMaxCardinality() {}

    /** builder initialized from an existing object
     * 
     * @param expected
     *            the existing object */
    public BuilderDataMaxCardinality(OWLDataMaxCardinality expected) {
        withCardinality(expected.getCardinality()).withProperty(expected.getProperty())
                .withRange(expected.getFiller());
    }

    /** @param arg
     *            cardinality
     * @return builder */
    public BuilderDataMaxCardinality withCardinality(int arg) {
        cardinality = arg;
        return this;
    }

    @Override
    public OWLDataMaxCardinality buildObject() {
        return df.getOWLDataMaxCardinality(cardinality, property, dataRange);
    }
}
