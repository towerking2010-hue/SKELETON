package com.example.engine.bio

import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting
import java.util.UUID

object BioscienceModule {

  fun getBioscienceCuratedCards(targetLanguage: LanguageSetting = LanguageSetting.ENGLISH): List<FeedCard> {
    val si = targetLanguage == LanguageSetting.SINHALA
    return listOf(
      FeedCard(
        id = "bio_optogenetics",
        title = if (si) "ආලෝකයෙන් ස්නායු සෛල පාලනය කිරීම" else "Optogenetics (Channelrhodopsin-2)",
        itch = if (si) "සතෙකු අවදිව සිටින විට, අවට ඇති අනෙකුත් ස්නායු සෛලවලට බලපෑමක් නොකර, තෝරාගත් එක් ස්නායු සෛල කණ්ඩායමක් ඉතා කෙටි කාලයකට පමණක් ක්‍රියාත්මක කරන්නේ කෙසේද?" else "How do you activate a single, specific neural circuit in an awake, behaving animal with millisecond temporal precision without perturbing surrounding neurons?",
        skeleton = if (si) "පළමුව තෝරාගත් ස්නායු සෛල තුළ ආලෝකයට ප්‍රතිචාර දක්වන ප්‍රෝටීනයක් නිපදවයි. නිල් ආලෝකය වැටුණු විට එම ප්‍රෝටීනයේ අයන ගමන් කරන දොරටුව විවෘත වේ. සෝඩියම් සහ කැල්සියම් අයන සෛලය තුළට ඇතුළු වීමෙන් සෛලයේ විද්‍යුත් තත්ත්වය වෙනස් වී ස්නායු සංඥාවක් ඇති වේ. මෙය තෝරාගත් සෛලවල පමණක් සිදුවන නිසා ඒවායේ කාර්යය වෙන්කර අධ්‍යයනය කළ හැක." else "ChR2 (algal light-gated cation channel) expressed via Cre-lox driver -> 470nm blue light photon absorbs into all-trans retinal -> 13-cis isomerization opens 6Å pore -> passive inward Na+/Ca2+ current -> rapid depolarization past -55mV threshold triggering action potential.",
        intuition = if (si) "එක් කාමරයක විදුලි පහනකට පමණක් වෙනම ස්විචයක් සවි කළා වගේ. නිල් ආලෝකය යොමු කළ විට තෝරාගත් ස්නායු සෛලය ක්‍රියාත්මක වන අතර අසල ඇති සෛල එලෙසම පවතී." else "Installing a light switch directly inside the wall of a single neuron; flicking a blue flashlight turns on that specific cell while leaving the rest of the brain dark.",
        branchDomain = if (si) "ජීව විද්‍යාව: ස්නායු පද්ධතිය" else "Bioscience: Neurobiology",
        crossLinkTarget = if (si) "ස්නායු සම්බන්ධතා ශක්තිමත් වීම" else "Synaptic Plasticity",
        isSaved = true,
        recallStage = 2,
        challengeScenario = if (si) "මෙම ප්‍රෝටීනයට ගැළපෙන නිල් ආලෝකය වෙනුවට වෙනත් වර්ණයක කහ ආලෝකයක් යොමු කළහොත් ස්නායු සෛලය ක්‍රියාත්මක නොවන්නේ ඇයි?",
        challengeExpectedKey = if (si) "මෙම ප්‍රෝටීනය නිල් ආලෝකයට විශේෂයෙන් ප්‍රතිචාර දක්වයි. වෙනත් වර්ණයක ආලෝකය එයට අවශ්‍ය හැඩ වෙනස්වීම ඇති නොකරන නිසා දොරටුව විවෘත නොවේ." else "ChR2 absorption spectrum peaks at 470nm; 589nm activates halorhodopsin (chloride pump) or causes no isomerizing transition.",
        language = if (si) "si" else "en"
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
