package decomposition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.MultiMap;

/** signature index
 * 
 * @author ignazio */
public class SigIndex {
    /** map between entities and axioms that contains them in their signature */
    private MultiMap<OWLEntity, AxiomWrapper> Base = new MultiMap<OWLEntity, AxiomWrapper>();
    /** locality checker */
    private LocalityChecker checker;
    /** sets of axioms non-local wrt the empty signature */
    private List<AxiomWrapper> NonLocalTrue = new ArrayList<AxiomWrapper>();
    private List<AxiomWrapper> NonLocalFalse = new ArrayList<AxiomWrapper>();
    /** empty signature to test the non-locality */
    private Signature emptySig = new Signature();
    /** number of registered axioms */
    private int nRegistered = 0;

    // access to statistics
    /** @return number of ever processed axioms */
    public int nProcessedAx() {
        return nRegistered;
    }

    /** add axiom AX to the non-local set with top-locality value TOP */
    private void checkNonLocal(AxiomWrapper ax, boolean top) {
        emptySig.setLocality(top);
        checker.setSignatureValue(emptySig);
        if (!checker.local(ax.getAxiom())) {
            if (top) {
                NonLocalFalse.add(ax);
            } else {
                NonLocalTrue.add(ax);
            }
        }
    }

    /** @param c
     *            locality checker */
    public SigIndex(LocalityChecker c) {
        checker = c;
    }

    // work with axioms
    /** register an axiom */
    private void registerAx(AxiomWrapper ax) {
        for (OWLEntity p : ax.getAxiom().getSignature()) {
            Base.put(p, ax);
        }
        // check whether the axiom is non-local
        checkNonLocal(ax, false);
        checkNonLocal(ax, true);
        ++nRegistered;
    }

    /** unregister an axiom AX */
    private void unregisterAx(AxiomWrapper ax) {
        for (OWLEntity p : ax.getAxiom().getSignature()) {
            Base.remove(p, ax);
        }
        // remove from the non-locality
        NonLocalFalse.remove(ax);
        NonLocalTrue.remove(ax);
    }

    /** process an axiom wrt its Used status
     * 
     * @param ax
     *            the axiom to process */
    public void processAx(AxiomWrapper ax) {
        if (ax.isUsed()) {
            registerAx(ax);
        } else {
            unregisterAx(ax);
        }
    }

    /** preprocess given set of axioms
     * 
     * @param axioms
     *            the axioms to process */
    public void preprocessOntology(Collection<AxiomWrapper> axioms) {
        for (AxiomWrapper ax : axioms) {
            processAx(ax);
        }
    }

    /** clear internal structures */
    public void clear() {
        Base.clear();
        NonLocalFalse.clear();
        NonLocalTrue.clear();
    }

    // get the set by the index
    /** given an entity, return a set of all axioms that contain this entity in a
     * signature
     * 
     * @param entity
     *            the entity
     * @return collection of axioms referring the entity */
    public Collection<AxiomWrapper> getAxioms(OWLEntity entity) {
        return Base.get(entity);
    }

    /** get the non-local axioms with top-locality value TOP
     * 
     * @param top
     *            true if top locality should be used
     * @return collection of non local axioms */
    public Collection<AxiomWrapper> getNonLocal(boolean top) {
        return top ? NonLocalFalse : NonLocalTrue;
    }
}
