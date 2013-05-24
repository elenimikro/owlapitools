package org.semanticweb.owlapitools.builders;

import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;

/** Builder class for SWRLSameIndividualAtom */
public class BuilderSWRLSameIndividualAtom extends
        BaseBuilder<SWRLSameIndividualAtom, BuilderSWRLSameIndividualAtom> {
    private SWRLIArgument arg1;
    private SWRLIArgument arg0;

    /** builder initialized from an existing object
     * 
     * @param expected
     *            the existing object */
    public BuilderSWRLSameIndividualAtom(SWRLSameIndividualAtom expected) {
        withArg0(expected.getFirstArgument()).withArg1(expected.getSecondArgument());
    }

    /** uninitialized builder */
    public BuilderSWRLSameIndividualAtom() {}

    /** @param arg
     *            individual
     * @return builder */
    public BuilderSWRLSameIndividualAtom withArg0(SWRLIArgument arg) {
        arg0 = arg;
        return this;
    }

    /** @param arg
     *            individual
     * @return builder */
    public BuilderSWRLSameIndividualAtom withArg1(SWRLIArgument arg) {
        arg1 = arg;
        return this;
    }

    @Override
    public SWRLSameIndividualAtom buildObject() {
        return df.getSWRLSameIndividualAtom(arg0, arg1);
    }
}
