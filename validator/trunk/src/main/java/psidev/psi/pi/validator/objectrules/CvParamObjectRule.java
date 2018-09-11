package psidev.psi.pi.validator.objectrules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import psidev.psi.pi.validator.objectrules.util.OBOFileReader;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.validator.Context;
import psidev.psi.tools.validator.MessageLevel;
import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;

/**
 * Checks the CVParams.
 * 
 * @author Gerhard Mayer, MPC, Bochum
 */
public class CvParamObjectRule extends AObjectRule<CvParam> {

    /**
     * Constants.
     */
    private static final Context CVPARAM_CONTEXT = new Context(MzIdentMLElement.CvParam.getXpath());
    private static final String STR_COLON   = ":";
    private static OBOFileReader OFR = new OBOFileReader();
    
    /**
     * Constructor.
     */
    public CvParamObjectRule() {
        this(null);
    }

    /**
     * Constructor.
     * @param ontologyManager the ontology manager
     */
    public CvParamObjectRule(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    /**
     * Checks, if the object is a CvParam.
     * 
     * @param obj   the object to check
     * @return true, if obj is a CvParam
     */
    @Override
    public boolean canCheck(Object obj) {
        return (obj instanceof CvParam);
    }

    /**
     * Checks the CvParam element.
     * @param cvParam the CvParam element
     * @return collection of messages
     * @throws ValidatorException validator exception
     */
    @Override
    public Collection<ValidatorMessage> check(CvParam cvParam) throws ValidatorException {
        List<ValidatorMessage> messages = new ArrayList<>();

        if (cvParam != null) {
            String acc = cvParam.getAccession();
            String cvName = cvParam.getName();

            // checks for empty accession number
            if (acc.isEmpty()) {
                messages.add(this.getEmptyAccessionMsg(cvName));
            }
            else {
                int pos = acc.indexOf(CvParamObjectRule.STR_COLON);
                String ontID = acc.substring(0, pos);
                String termID = acc.substring(pos + 1);

                // checks if the CV term name is valid
                if (!OBOFileReader.isValidCVTermName(ontID, termID, cvName)) {
                    messages.add(this.getWrongCVTermNameMsg(cvParam, ontID, termID));
                }

                // checks if the CV term has a value, when it should have one
                if (OBOFileReader.hasCVTermAValue(ontID, termID)) {
                    messages.add(this.getMissingCVTermValueMsg(cvParam));
                }
            }
        }
        
        return messages;
    }

   /**
     * Gets the validator message for an empty CV accession.
     * @param cvName
     * @return the ValidatorMessage
     */
    private ValidatorMessage getEmptyAccessionMsg(String cvName) {
        String strB = "The cvParam for " + cvName + " has an empty accession.";
        
        return new ValidatorMessage(strB, MessageLevel.ERROR, CvParamObjectRule.CVPARAM_CONTEXT, this);        
    }
    
   /**
     * Gets the validator message for a wrong CV term name.
     * @param cvParam
     * @param ontID     the ontology ID
     * @param termID    the CV term ID
     * @return the ValidatorMessage
     */
    private ValidatorMessage getWrongCVTermNameMsg(CvParam cvParam, String ontID, String termID) {
        String strB = "A cvParam for " + cvParam.getAccession() + " has an invalid name: " + cvParam.getName()+ ". It should be: " + OBOFileReader.getCVTermNameFromID(ontID, termID);
        
        return new ValidatorMessage(strB, MessageLevel.ERROR, CvParamObjectRule.CVPARAM_CONTEXT, this);        
    }
    
   /**
     * Gets the validator message for a wrong CV term name.
     * @param cvParam
     * @return the ValidatorMessage
     */
    private ValidatorMessage getMissingCVTermValueMsg(CvParam cvParam) {
        String strB = "A cvParam for " + cvParam.getAccession() + " has a missing value where it should have one. ";
        
        return new ValidatorMessage(strB, MessageLevel.ERROR, CvParamObjectRule.CVPARAM_CONTEXT, this);        
    }
    
    /**
     * Gets the tips how to fix the error.
     * 
     * @return collection of tips
     */
    @Override
    public Collection<String> getHowToFixTips() {
        List<String> ret = new ArrayList<>();

        ret.add("Provide the newest version for all cv element under the CvParam element." + CvParamObjectRule.CVPARAM_CONTEXT.getContext());

        return ret;
    }
}
