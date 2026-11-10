package com.example.engine.bio

import com.example.data.model.FeedCard
import java.util.UUID

object BioscienceModule {

  fun getBioscienceCuratedCards(): List<FeedCard> {
    return listOf(
      FeedCard(
        id = "bio_optogenetics",
        title = "Optogenetics (Channelrhodopsin-2)",
        itch = "How do you activate a single, specific neural circuit in an awake, behaving animal with millisecond temporal precision without perturbing surrounding neurons?",
        skeleton = "ChR2 (algal light-gated cation channel) expressed via Cre-lox driver -> 470nm blue light photon absorbs into all-trans retinal -> 13-cis isomerization opens 6Å pore -> passive inward Na+/Ca2+ current -> rapid depolarization past -55mV threshold triggering action potential.",
        intuition = "Installing a light switch directly inside the wall of a single neuron; flicking a blue flashlight turns on that specific cell while leaving the rest of the brain dark.",
        branchDomain = "Bioscience: Neurobiology",
        crossLinkTarget = "Synaptic Plasticity",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "If you switch the photostimulation laser from 470nm to 589nm on a ChR2-expressing neuron, why does the cell fail to fire?",
        challengeExpectedKey = "ChR2 absorption spectrum peaks at 470nm; 589nm activates halorhodopsin (chloride pump) or causes no isomerizing transition.",
        language = "en"
      ),
      FeedCard(
        id = "bio_crispr_base_editing",
        title = "CRISPR-Cas9 Base Editing",
        itch = "Standard Cas9 introduces double-stranded DNA breaks (DSBs), causing uncontrollable insertions and deletions (indels) via non-homologous end joining (NHEJ). How do you edit a single nucleotide without cutting both strands?",
        skeleton = "Catalytically dead Cas9 (dCas9) or nickase (nCas9-D10A) fused to cytidine deaminase (APOBEC/rAPOBEC1) or adenine deaminase (TadA). Single gRNA unwinds R-loop -> deaminase converts C->U (read as T) or A->I (read as G) within a 5nt window without double-strand break.",
        intuition = "Instead of cutting the DNA ribbon in half with scissors and taping it back together (wildtype Cas9), you use an ultra-fine chemical pencil eraser that directly alters a single letter while leaving the ribbon intact.",
        branchDomain = "Bioscience: Molecular Genetics",
        crossLinkTarget = "Epigenetic Methylation",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Why does nicking the non-edited strand (nCas9-D10A) increase the conversion efficiency of the deaminated base?",
        challengeExpectedKey = "Tricks host mismatch repair (MMR) machinery to degrade and resynthesize the non-edited strand using the edited strand as template.",
        language = "en"
      ),
      FeedCard(
        id = "bio_ltp_nmda",
        title = "NMDA Coincidence Gating (LTP)",
        itch = "What is the physical biophysical coincidence detector that allows neurons to store associations (Hebbian learning: 'cells that fire together, wire together')?",
        skeleton = "At resting potential (-70mV), hydrated Mg2+ ions physically plug the NMDA channel pore. Depolarization of postsynaptic membrane (via AMPA receptor Na+ influx) expels the Mg2+ plug electrostatically. Simultaneous glutamate binding + absence of Mg2+ allows massive Ca2+ influx -> activates CaMKII -> inserts new AMPA receptors into postsynaptic density.",
        intuition = "A bank vault with two distinct keyholes: one key is turned by the chemical messenger (glutamate), the other key is turned by electricity (voltage unblocking the Mg2+ stone).",
        branchDomain = "Bioscience: Synaptic Biophysics",
        crossLinkTarget = "Dendritic Spine Remodeling",
        isSaved = true,
        recallStage = 3,
        challengeScenario = "What happens to long-term potentiation if an antagonist irreversibly binds the glycine co-agonist site of the NMDA receptor?",
        challengeExpectedKey = "LTP induction is blocked because glycine binding is obligatory for NMDA channel opening even with glutamate and membrane depolarization present.",
        language = "en"
      ),
      FeedCard(
        id = "bio_crispr_prime_editing",
        title = "Prime Editing (PE2/PE3)",
        itch = "Base editors can only perform 4 transition mutations (C->T, G->A, A->G, T->C). How do you write all 12 possible base substitutions and arbitrary insertions/deletions without double-stranded breaks?",
        skeleton = "nCas9 fused to engineered M-MLV Reverse Transcriptase + pegRNA (prime editing guide RNA carrying both targeting spacer, primer binding site, and reverse transcriptase template). Nicked 3' flap hybridizes to PBS -> RT copies pegRNA sequence directly into genomic DNA -> flap equilibrium resolves into genome.",
        intuition = "A search-and-replace word processor for DNA: the guide finds the exact paragraph, lifts the text flap, and types in the replacement sentence directly off its own template ribbon.",
        branchDomain = "Bioscience: Genetic Engineering",
        crossLinkTarget = "CRISPR-Cas9 Base Editing",
        isSaved = false,
        recallStage = 0,
        challengeScenario = "In the PE3 system, what purpose does making a second nick in the unedited strand serve?",
        challengeExpectedKey = "Forces host mismatch repair to preferentially replace the non-edited strand using the newly reverse-transcribed prime-edited flap as the repair template.",
        language = "en"
      )
    )
  }
}
