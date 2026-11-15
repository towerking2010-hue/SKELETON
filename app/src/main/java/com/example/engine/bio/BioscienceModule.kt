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
        challengeScenario = if (si) "මෙම ප්‍රෝටීනයට ගැළපෙන නිල් ආලෝකය වෙනුවට වෙනත් වර්ණයක කහ ආලෝකයක් යොමු කළහොත් ස්නායු සෛලය ක්‍රියාත්මක නොවන්නේ ඇයි?" else "Why would a different light colour fail to activate the selected neurons?",
        challengeExpectedKey = if (si) "මෙම ප්‍රෝටීනය නිල් ආලෝකයට විශේෂයෙන් ප්‍රතිචාර දක්වයි. වෙනත් වර්ණයක ආලෝකය එයට අවශ්‍ය හැඩ වෙනස්වීම ඇති නොකරන නිසා දොරටුව විවෘත නොවේ." else "ChR2 absorption spectrum peaks at 470nm; 589nm activates halorhodopsin (chloride pump) or causes no isomerizing transition.",
        language = if (si) "si" else "en"
      ),
      FeedCard(
        id = "bio_crispr_base_editing",
        title = if (si) "ජානවල තනි අකුරක් වෙනස් කිරීම" else "CRISPR-Cas9 Base Editing",
        itch = if (si) "සාමාන්‍ය ජාන සංස්කරණ ක්‍රමයකදී DNA දාම දෙකම කපා දමයි. එවිට අනවශ්‍ය අකුරු ඇතුළත් වීම හෝ මැකී යාම සිදුවිය හැක. DNA දාම දෙකම නොකපා එහි එක් අකුරක් පමණක් වෙනස් කළ හැක්කේ කෙසේද?" else "Standard Cas9 introduces double-stranded DNA breaks (DSBs), causing uncontrollable insertions and deletions (indels) via non-homologous end joining (NHEJ). How do you edit a single nucleotide without cutting both strands?",
        skeleton = if (si) "මඟ පෙන්වන කුඩා අණුවක් අවශ්‍ය ජාන ස්ථානය සොයාගනී. එයට සම්බන්ධ කළ එන්සයිමයක් තෝරාගත් DNA අකුරේ රසායනික හැඩය වෙනස් කරයි. සෛලය එම වෙනස නව අකුරක් ලෙස කියවීමට සලස්වයි. මෙහිදී DNA දාම දෙකම කපා දැමීම අවශ්‍ය නොවේ." else "Catalytically dead Cas9 (dCas9) or nickase (nCas9-D10A) fused to cytidine deaminase (APOBEC/rAPOBEC1) or adenine deaminase (TadA). Single gRNA unwinds R-loop -> deaminase converts C->U (read as T) or A->I (read as G) within a 5nt window without double-strand break.",
        intuition = if (si) "පොතක පිටුවක් ඉරා නැවත අලවනවා වෙනුවට, අවශ්‍ය එක අකුර පමණක් මකලා නිවැරදි අකුර ලියන ඉතා සියුම් පැන්සලක් භාවිත කරනවා වගේ." else "Instead of cutting the DNA ribbon in half with scissors and taping it back together (wildtype Cas9), you use an ultra-fine chemical pencil eraser that directly alters a single letter while leaving the ribbon intact.",
        branchDomain = if (si) "ජීව විද්‍යාව: අණුක ජාන විද්‍යාව" else "Bioscience: Molecular Genetics",
        crossLinkTarget = if (si) "ජාන ක්‍රියාකාරිත්වය පාලනය වීම" else "Epigenetic Methylation",
        isSaved = true,
        recallStage = 1,
        challengeScenario = if (si) "වෙනස් නොකළ DNA දාමයේ ඉතා කුඩා කැපුමක් කිරීමෙන් අපේක්ෂිත අකුරු වෙනස්වීම වැඩි වශයෙන් සිදුවන්නේ ඇයි?" else "Why can nicking the unedited DNA strand improve the intended base conversion?",
        challengeExpectedKey = if (si) "සෛලයේ අලුත්වැඩියා ක්‍රියාවලියට වෙනස් කළ දාමය නිවැරදි ආදර්ශය ලෙස භාවිත කිරීමට එම කුඩා කැපුම උපකාරී වේ." else "Tricks host mismatch repair (MMR) machinery to degrade and resynthesize the non-edited strand using the edited strand as template.",
        language = "en"
      ),
      FeedCard(
        id = "bio_ltp_nmda",
        title = if (si) "මතකය සෑදෙන විට ස්නායු සෛල දෙකක් එකවර ක්‍රියා කිරීම" else "NMDA Coincidence Gating (LTP)",
        itch = if (si) "අත්දැකීමක් මතකයක් බවට පත්වන විට, එකිනෙකට සම්බන්ධ ස්නායු සෛල දෙකක් එකම වේලාවේ ක්‍රියා කළ බව මොළය හඳුනාගන්නේ කෙසේද?" else "What is the physical biophysical coincidence detector that allows neurons to store associations (Hebbian learning: 'cells that fire together, wire together')?",
        skeleton = if (si) "විවේකයෙන් සිටින ස්නායු සෛලයක මෙම දොරටුව මැග්නීසියම් අංශුවකින් අවහිර වී ඇත. අසල්වැසි සෛලයෙන් ලැබෙන සංඥාව නිසා සෛලයේ විද්‍යුත් තත්ත්වය වෙනස් වූ විට එම අවහිරය ඉවත් වේ. ඒ සමඟ රසායනික පණිවිඩකරු බැඳී ඇත්නම් කැල්සියම් සෛලය තුළට ඇතුළු වේ. එය සම්බන්ධතාවය ශක්තිමත් කරන වෙනස්කම් ආරම්භ කරයි." else "At resting potential (-70mV), hydrated Mg2+ ions physically plug the NMDA channel pore. Depolarization of postsynaptic membrane (via AMPA receptor Na+ influx) expels the Mg2+ plug electrostatically. Simultaneous glutamate binding + absence of Mg2+ allows massive Ca2+ influx -> activates CaMKII -> inserts new AMPA receptors into postsynaptic density.",
        intuition = if (si) "දොරක් විවෘත කිරීමට යතුරු දෙකක් අවශ්‍යයි කියලා සිතන්න. එක් යතුර රසායනික පණිවිඩයෙන් හැරවෙනවා. අනෙක් යතුර සෛලයේ විද්‍යුත් වෙනසෙන් හැරවෙනවා. දෙකම සිදුවූ විට පමණක් දොර විවෘත වෙනවා." else "A bank vault with two distinct keyholes: one key is turned by the chemical messenger (glutamate), the other key is turned by electricity (voltage unblocking the Mg2+ stone).",
        branchDomain = if (si) "ජීව විද්‍යාව: ස්නායු සම්බන්ධතා" else "Bioscience: Synaptic Biophysics",
        crossLinkTarget = if (si) "ස්නායු සම්බන්ධතා වෙනස් වීම" else "Dendritic Spine Remodeling",
        isSaved = true,
        recallStage = 3,
        challengeScenario = if (si) "මෙම දොරටුව ක්‍රියාත්මක වීමට අවශ්‍ය රසායනික ද්‍රව්‍යයක් එයට බැඳීමට නොහැකි කළහොත් දිගුකාලීන මතක සම්බන්ධතාවය ශක්තිමත් වීම නතර වන්නේ ඇයි?" else "Why does blocking the required ligand stop synaptic strengthening?",
        challengeExpectedKey = if (si) "අවශ්‍ය රසායනික ද්‍රව්‍යය බැඳීම අත්‍යවශ්‍ය පියවරක් බැවින්, අනෙක් සංඥා දෙක තිබුණත් දොරටුව විවෘත නොවේ. එබැවින් සම්බන්ධතාවය ශක්තිමත් කිරීම ආරම්භ නොවේ." else "LTP induction is blocked because glycine binding is obligatory for NMDA channel opening even with glutamate and membrane depolarization present.",
        language = "en"
      ),
      FeedCard(
        id = "bio_crispr_prime_editing",
        title = if (si) "DNA තුළ නිශ්චිත වෙනස්කම් ලිවීම" else "Prime Editing (PE2/PE3)",
        itch = if (si) "ජානවල අකුරු කිහිපයක් එකවර වෙනස් කිරීමට හෝ අලුත් අකුරු ඇතුළත් කිරීමට අවශ්‍ය වූ විට, DNA දාම දෙකම කපා නොදමා ඒ වෙනස්කම් නිවැරදි ස්ථානයට ලියන්නේ කෙසේද?" else "Base editors can only perform 4 transition mutations (C->T, G->A, A->G, T->C). How do you write all 12 possible base substitutions and arbitrary insertions/deletions without double-stranded breaks?",
        skeleton = if (si) "මඟ පෙන්වන අණුවක් DNA හි නිවැරදි ස්ථානය සොයාගනී. ඉන්පසු එයට සම්බන්ධ එන්සයිමයක් අවශ්‍ය නව ජාන අනුපිළිවෙළ පිටපත් කර DNA තුළට ඇතුළත් කරයි. මෙය මුළු DNA දාමයම කපා දැමීමකින් තොරව සිදු කළ හැක." else "nCas9 fused to engineered M-MLV Reverse Transcriptase + pegRNA (prime editing guide RNA carrying both targeting spacer, primer binding site, and reverse transcriptase template). Nicked 3' flap hybridizes to PBS -> RT copies pegRNA sequence directly into genomic DNA -> flap equilibrium resolves into genome.",
        intuition = if (si) "ලිපියක වැරදි වචනය සොයාගෙන, මුළු පිටුවම ඉරා නොදමා, එම ස්ථානයේ නිවැරදි වචනය ලියා තබන සංස්කාරකයක් වගේ." else "A search-and-replace word processor for DNA: the guide finds the exact paragraph, lifts the text flap, and types in the replacement sentence directly off its own template ribbon.",
        branchDomain = if (si) "ජීව විද්‍යාව: ජාන සංස්කරණය" else "Bioscience: Genetic Engineering",
        crossLinkTarget = if (si) "ජානවල තනි අකුරු වෙනස් කිරීම" else "CRISPR-Cas9 Base Editing",
        isSaved = false,
        recallStage = 0,
        challengeScenario = if (si) "වෙනස් නොකළ DNA දාමයේ දෙවැනි කුඩා කැපුමක් කිරීමෙන් නිවැරදි නව අනුපිළිවෙළ පිළිගැනීමට සෛලයට උපකාර වන්නේ කෙසේද?" else "How does nicking the opposite strand help retain the newly written DNA sequence?",
        challengeExpectedKey = if (si) "සෛලයේ අලුත්වැඩියා පද්ධතියට අලුතින් ලියූ අනුපිළිවෙළ ආදර්ශය ලෙස ගෙන අනෙක් දාමයද ඒ අනුව සකස් කිරීමට එය මඟ පෙන්වයි." else "Forces host mismatch repair to preferentially replace the non-edited strand using the newly reverse-transcribed prime-edited flap as the repair template.",
        language = "en"
      )
    )
  }
}
