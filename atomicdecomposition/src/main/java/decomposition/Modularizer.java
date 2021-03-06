package decomposition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

/** class to create modules of an ontology wrt module type */
public class Modularizer {
    /** shared signature signature */
    private Signature sig;
    /** internal syntactic locality checker */
    private LocalityChecker checker;
    /** module as a list of axioms */
    private List<AxiomWrapper> module = new ArrayList<AxiomWrapper>();
    /** pointer to a sig index; if not NULL then use optimized algo */
    private SigIndex sigIndex = null;
    /** queue of unprocessed entities */
    private Queue<OWLEntity> workQueue;

    /** update SIG wrt the axiom signature */
    private void addAxiomSig(AxiomWrapper axiom) {
        if (sigIndex != null) {
            for (OWLEntity p : axiom.getAxiom().getSignature()) {
                if (sig.add(p)) {
                    workQueue.add(p);
                }
            }
        }
    }

    /** add an axiom to a module */
    private void addAxiomToModule(AxiomWrapper axiom) {
        axiom.setInModule(true);
        module.add(axiom);
        // update the signature
        addAxiomSig(axiom);
    }

    /** @return true iff an AXiom is non-local */
    private boolean isNonLocal(OWLAxiom ax) {
        return !checker.local(ax);
    }

    /** add an axiom if it is non-local (or if noCheck is true) */
    private void addNonLocal(AxiomWrapper ax, boolean noCheck) {
        if (noCheck || isNonLocal(ax.getAxiom())) {
            addAxiomToModule(ax);
        }
    }

    /** add all the non-local axioms from given axiom-set AxSet */
    private void addNonLocal(Collection<AxiomWrapper> AxSet, boolean noCheck) {
        for (AxiomWrapper q : AxSet) {
            if (!q.isInModule() && q.isInSearchSpace()) {
                this.addNonLocal(q, noCheck);
            }
        }
    }

    /** build a module traversing axioms by a signature */
    private void extractModuleQueue() {
        // init queue with a sig
        workQueue.addAll(sig.getSignature());
        // add all the axioms that are non-local wrt given value of a
        // top-locality
        this.addNonLocal(sigIndex.getNonLocal(sig.topCLocal()), true);
        // main cycle
        while (!workQueue.isEmpty()) {
            // for all the axioms that contains entity in their signature
            Collection<AxiomWrapper> axioms = sigIndex.getAxioms(workQueue.poll());
            this.addNonLocal(axioms, false);
        }
    }

    /** extract module wrt presence of a sig index */
    private void extractModule(List<AxiomWrapper> args) {
        module.clear();
        // clear the module flag in the input
        final int size = args.size();
        for (int i = 0; i < size; i++) {
            AxiomWrapper p = args.get(i);
            p.setInModule(false);
            if (p.isUsed()) {
                p.setInSearchSpace(true);
            }
        }
        extractModuleQueue();
        for (int i = 0; i < size; i++) {
            args.get(i).setInSearchSpace(false);
        }
    }

    /** @param c
     *            the clocality checker */
    public Modularizer(LocalityChecker c) {
        checker = c;
        sig = c.getSignature();
        sigIndex = new SigIndex(checker);
    }

    /** allow the checker to preprocess an ontology if necessary
     * 
     * @param axioms
     *            list of wrapped axioms */
    public void preprocessOntology(Collection<AxiomWrapper> axioms) {
        checker.preprocessOntology(axioms);
        sigIndex.clear();
        sigIndex.preprocessOntology(axioms);
        workQueue = new ArrayDeque<OWLEntity>(axioms.size());
    }

    /** @param ax
     * @param type
     * @return true iff the axiom AX is a tautology wrt given type */
    public boolean isTautology(OWLAxiom ax, ModuleType type) {
        boolean topLocality = type == ModuleType.TOP;
        sig = new Signature(ax.getSignature());
        sig.setLocality(topLocality);
        // axiom is a tautology if it is local wrt its own signature
        boolean toReturn = checker.local(ax);
        if (type != ModuleType.STAR || !toReturn) {
            return toReturn;
        }
        // here it is STAR case and AX is local wrt BOT
        sig.setLocality(!topLocality);
        return checker.local(ax);
    }

    /** @return the Locality checker */
    public LocalityChecker getLocalityChecker() {
        return checker;
    }

    /** @param axiom
     * @param signature
     * @param type */
    public void extract(AxiomWrapper axiom, Signature signature, ModuleType type) {
        this.extract(Collections.singletonList(axiom), signature, type);
    }

    /** extract module wrt SIGNATURE and TYPE from the set of axioms
     * 
     * @param axioms
     * @param signature
     * @param type */
    public void extract(List<AxiomWrapper> axioms, Signature signature, ModuleType type) {
        boolean topLocality = type == ModuleType.TOP;
        sig = signature;
        checker.setSignatureValue(sig);
        sig.setLocality(topLocality);
        extractModule(axioms);
        if (type != ModuleType.STAR) {
            return;
        }
        // here there is a star: do the cycle until stabilization
        int size;
        List<AxiomWrapper> oldModule = new ArrayList<AxiomWrapper>();
        do {
            size = module.size();
            oldModule.clear();
            oldModule.addAll(module);
            topLocality = !topLocality;
            sig = signature;
            sig.setLocality(topLocality);
            extractModule(oldModule);
        } while (size != module.size());
    }

    /** @return the last computed module */
    public List<AxiomWrapper> getModule() {
        return module;
    }
}
