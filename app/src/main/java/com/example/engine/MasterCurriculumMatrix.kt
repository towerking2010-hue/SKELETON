package com.example.engine

import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting

/**
 * SKELETON 16 CORE CURRICULUM NODES (PRE-LOADED MATRIX)
 * Foundational matrix locked into the pinnacle knowledge bank:
 * 1. Etymology & Linguistic Software
 * 2. Mythology (Greek, Egyptian, Norse, Eastern, Esoteric)
 * 3. Sacred Geometry & Universal Ratios (ϕ, Platonic Solids)
 * 4. Numerology & Archetypal Principles
 * 5. Gematria & Alphanumeric Encoding
 * 6. Alchemy & Psychological Transmutation
 * 7. The Trivium (Grammar, Logic, Rhetoric)
 * 8. The Quadrivium (Arithmetic, Geometry, Music, Astronomy)
 * 9. Freemasonry & Architectural Allegory
 * 10. Hermeticism & Universal Laws (Mentalism, Polarity, Rhythm, etc.)
 * 11. Metaphysics & Ontology
 * 12. Music Theory & Acoustic Harmonics
 * 13. Colour Theory & Electromagnetic Wave Spectra
 * 14. Visual Composition & Spatial Balance
 * 15. General Psychology & Cognitive Science
 * 16. Dark Psychology, Manipulation & Defense Mechanics
 */
object MasterCurriculumMatrix {

  fun getNodes(targetLanguage: LanguageSetting = LanguageSetting.ENGLISH): List<FeedCard> {
    val isSinhala = targetLanguage == LanguageSetting.SINHALA
    return listOf(
      // 0. Physics: Unified Dynamic Mechanics
      FeedCard(
        id = "master_00_physics",
        title = if (isSinhala) "Physics: Unified Dynamic Mechanics / භෞතික විද්‍යාව" else "Physics (Mechanics, Wave Spectra, Quantum)",
        itch = if (isSinhala)
          "භෞතික වස්තූන් එකිනෙක ස්පර්ශ වන්නේ නැත; ක්වොන්ටම් මට්ටමේදී ඉලෙක්ට්‍රෝන වල Pauli Exclusion මූලධර්මය සහ විද්‍යුත් චුම්භක විකර්ෂණය නිසා ස්පර්ශය පිළිබඳ මායාවක් මොළය තුළ ජනනය වේ."
        else
          "Physical objects never actually touch; at the quantum boundary, the Pauli Exclusion Principle and electrostatic repulsion fabricate the macroscopic illusion of solid matter.",
        skeleton = if (isSinhala)
          "Classical Lagrangian mechanics (L = T - V) -> Maxwell's Field Equations (nabla x E = -dB/dt) -> Schrodinger Wave Equation (i*hbar*d_psi/dt = H*psi). ශක්තිය සහ තරංග-අංශු ද්විත්වතාවය (de Broglie: lambda = h/p)."
        else
          "Classical Lagrangian mechanics (L = T - V) -> Maxwell's field equations -> Schrodinger wave equation (i*hbar*d_psi/dt = H*psi). Wave-particle duality (de Broglie: lambda = h/p) and invariant conservation laws (Noether's Theorem).",
        intuition = if (isSinhala)
          "විශ්වයේ Source Code එක: කාලය, අවකාශය සහ පදාර්ථය අතර ශක්තිය නිරන්තරයෙන් හුවමාරු වන සංවෘත ඇල්ගොරිතමයකි."
        else
          "The universe's source code: an immutable rule engine where energy transforms continuously across space and time coordinates without net loss.",
        branchDomain = "Fundamental Physics",
        crossLinkTarget = "Sacred Geometry & Universal Ratios",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, why does conservation of energy mathematically follow from the time-translation symmetry of physical laws?",
        challengeExpectedKey = "Noether's Theorem: every continuous differentiable symmetry in the action yields a conserved invariant physical current.",
        language = if (isSinhala) "si" else "en"
      ),

      // 0. Chemistry: Molecular Architecture & Energetics
      FeedCard(
        id = "master_00_chemistry",
        title = if (isSinhala) "Chemistry: Molecular Architecture & Energetics / රසායන විද්‍යාව" else "Chemistry (Atomic Geometry & Energetics)",
        itch = if (isSinhala)
          "රසායනික ප්‍රතික්‍රියාවක් යනු අහඹු මිශ්‍රණයක් නොවේ; එය ඉලෙක්ට්‍රෝන කක්ෂ වල ශක්ති මට්ටම් අවම කරගැනීමට දරන ස්වයංක්‍රීය Thermodynamic Optimization ක්‍රියාවලියකි."
        else
          "Chemical reactions are not random collisions; they are deterministic thermodynamic optimization sweeps seeking the lowest Gibbs free energy state.",
        skeleton = if (isSinhala)
          "Gibbs Free Energy Delta_G = Delta_H - T*Delta_S (Delta_G < 0 නම් Spontaneous). VSEPR ජ්‍යාමිතිය (tetrahedral 109.5°, sp3, sp2, sp hybridization). Covalent vs Ionic bonding."
        else
          "Gibbs Free Energy Delta_G = Delta_H - T*Delta_S (spontaneous when Delta_G < 0). VSEPR molecular geometry, orbital hybridization (sp3, sp2, sp), and valence shell electronegativity differentials.",
        intuition = if (isSinhala)
          "කන්දකින් පහළට පෙරළෙන බෝලයක් වැනිය: පරමාණු නිරන්තරයෙන්ම අවම ශක්ති සහ උපරිම ස්ථායීතා මට්ටමට (Lowest Energy Well) වැටේ."
        else
          "A marble rolling into a bowl: atoms constantly reshuffle until they fall into the deepest potential energy well.",
        branchDomain = "Molecular Chemistry",
        crossLinkTarget = "Physics: Unified Dynamic Mechanics",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, if a reaction is endothermic (Delta_H > 0), under what exact condition can it still proceed spontaneously?",
        challengeExpectedKey = "At high temperature T when entropy gain Delta_S is positive such that T*Delta_S exceeds Delta_H, making Delta_G negative.",
        language = if (isSinhala) "si" else "en"
      ),

      // 0. Biology: Self-Organizing Cellular Cybernetics
      FeedCard(
        id = "master_00_biology",
        title = if (isSinhala) "Biology: Self-Organizing Cellular Cybernetics / ජීව විද්‍යාව" else "Biology (Cellular Energetics & Genetics)",
        itch = if (isSinhala)
          "ජීවය යනු තාපගති විද්‍යාවේ 2 වන නීතියට (Entropy) එරෙහිව පරිසරයෙන් නිදහස් ශක්තිය ලබාගෙන අභ්‍යන්තර පිළිවෙළක් (Negative Entropy) පවත්වාගන්නා ස්වයං-ප්‍රතිනිෂ්පාදක පද්ධතියකි."
        else
          "Life is a local thermodynamic rebellion against universal entropy, exporting disorder to maintain pristine internal order (Schrodinger's Negative Entropy).",
        skeleton = if (isSinhala)
          "Cellular energetics: ATP hydrolysis (ATP -> ADP + Pi, Delta_G = -30.5 kJ/mol). Mitochondrial proton-motive force (Chemiosmosis). Central Dogma (DNA -> RNA -> Protein). Homeostatic feedback loops."
        else
          "Cellular energetics: ATP synthase proton-motive force (Chemiosmosis, Delta_G = -30.5 kJ/mol). Central Dogma (DNA transcription -> mRNA translation -> Protein folding). Epigenetic chromatin remodeling.",
        intuition = if (isSinhala)
          "නිරන්තරයෙන් වතුර ඉවත් කරන බෝට්ටුවක් වැනිය: බෝට්ටුව තුළ වියලි පිළිවෙළක් තබාගැනීමට ශක්තිය වැය කරමින් ජලය පිටතට පොම්ප කරයි."
        else
          "A boat with a pump: expending energy to pump water outward so the interior stays dry and ordered against the surrounding lake.",
        branchDomain = "Cellular Biology",
        crossLinkTarget = "Chemistry: Molecular Architecture & Energetics",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, how does the mitochondrial inner membrane convert a spatial proton gradient into chemical ATP bonds?",
        challengeExpectedKey = "Protons flow through ATP synthase rotor, mechanically rotating the catalytic headpiece to condense ADP and inorganic phosphate into ATP.",
        language = if (isSinhala) "si" else "en"
      ),

      // 1. Etymology & Linguistic Software
      FeedCard(
        id = "master_01_etymology",
        title = if (isSinhala) "Etymology & Linguistic Software / භාෂා මෘදුකාංග" else "Etymology & Linguistic Software",
        itch = if (isSinhala)
          "භාෂාව හුදු වචන මාලාවක් නොවේ; එය මිනිස් උපවිඥානයේ දැක්ම පාලනය කරන Executable Cognitive Software එකකි."
        else
          "Language is not a descriptive dictionary; it is executable cognitive software that programs your subconscious perceptual boundaries.",
        skeleton = if (isSinhala)
          "PIE (Proto-Indo-European) මූලයන් -> Grimm's Law & Verner's Law (ශබ්ද විකෘතිතා). Sapir-Whorf linguistic relativity: වාක්‍ය ඛණ්ඩ රටා මඟින් සිතීමේ සීමාවන් සකස් කෙරේ."
        else
          "PIE roots (Proto-Indo-European) -> semantic drift & phonetic mutation (Grimm's Law, Verner's Law). Syntactic structures govern thought horizon: Sapir-Whorf linguistic relativity constraint.",
        intuition = if (isSinhala)
          "Compiler එකක් වැනිය: ව්‍යාකරණ syntax එක වෙනස් කළ විට පෙර ගණනය කළ නොහැකි වූ ඇල්ගොරිතම ක්ෂණික native functions බවට පත්වේ."
        else
          "A computer language compiler: change the grammar syntax rules and algorithms that were previously uncomputable become instant native functions.",
        branchDomain = "Linguistics & Mind",
        crossLinkTarget = "The Trivium",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, if a language lacks future-tense verb inflections, how does behavioral time-discounting shift in long-term capital savings?",
        challengeExpectedKey = "Speakers treat future self as identical to present self, dramatically increasing long-term savings rates.",
        language = if (isSinhala) "si" else "en"
      ),

      // 2. Mythology (Universal Psychodrama)
      FeedCard(
        id = "master_02_mythology",
        title = if (isSinhala) "Mythology: The Universal Psychodrama / මිථ්‍යා කථා" else "Mythology: The Universal Psychodrama",
        itch = if (isSinhala)
          "මිථ්‍යාවන් යනු ප්‍රාථමික ප්‍රබන්ධ නොවේ; ඒවා වසර දහස් ගණනක් පුරා මානව හැසිරීම් කේතනය කළ Archetypal Operating Manuals වේ."
        else
          "Myths are not primitive fairy tales; they are encrypted psychological operating manuals recording recurring structural human dynamics across millennia.",
        skeleton = if (isSinhala)
          "Solar-lunar dialectic + Hero Monomyth (Separation -> Initiation -> Return). Osiris කැබලි කිරීම (Entropy) -> Horus ගේ ඇස ප්‍රතිෂ්ඨාපනය (Conscious Awareness). Odin ගේ ඇස කැපකිරීම = රටා අවබෝධය වෙනුවෙන් සාමාන්‍ය දෘෂ්ටිය අත්හැරීම."
        else
          "Solar-lunar dialectic + Hero monomyth (Separation -> Initiation -> Return). Osiris dismemberment (entropy) -> Horus eye restoration (conscious awareness). Odin's eye sacrifice at Mimir's well = trading biological sight for pattern perception.",
        intuition = if (isSinhala)
          "වසර මිලියන ගණනක පැවැත්ම පිළිබඳ මානව අත්දැකීම්, බුද්ධිමය බාධක මඟහැර උපවිඥානයට ළඟාවන සංකේත බවට සම්පීඩනය කළ Compression Algorithm එකක් වැනිය."
        else
          "A compression algorithm: millions of years of human tribal survival compressed into symbolic narrative archetypes that bypass intellectual defenses.",
        branchDomain = "Esoteric Mythology",
        crossLinkTarget = "General Psychology",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, what psychological transition is symbolized by Odin hanging 9 days on Yggdrasil wounded by his own spear?",
        challengeExpectedKey = "Voluntary ego-death and self-sacrifice required to decode universal symbolic knowledge (the Runes).",
        language = if (isSinhala) "si" else "en"
      ),

      // 3. Sacred Geometry & Universal Ratios
      FeedCard(
        id = "master_03_sacred_geometry",
        title = if (isSinhala) "Sacred Geometry & Universal Ratios (ϕ) / පූජනීය ජ්‍යාමිතිය" else "Sacred Geometry & Universal Ratios (ϕ, Platonic Solids)",
        itch = if (isSinhala)
          "අභ්‍යවකාශය හිස් අවකාශයක් නොවේ; එය නොවෙනස්වන ගණිතමය අනුපාත මත පදනම් වූ Resonant Geometric Matrix එකකි."
        else
          "Space is not an empty void; it is a resonant geometric matrix governed by invariant mathematical proportions.",
        skeleton = if (isSinhala)
          "Golden Ratio ϕ = (1 + sqrt(5))/2 ≈ 1.6180339... Platonic Solids: R^3 හි හරියටම නිත්‍ය බහුතල 5ක් පමණි (Euler's formula V - E + F = 2). සියලු ත්‍රිමාණ අවකාශ ව්‍යුහයන් මේවායින් සීමා වේ."
        else
          "Golden Ratio ϕ = (1 + sqrt(5))/2 ≈ 1.6180339887... Platonic Solids: exactly 5 regular polyhedra in R^3 (Tetrahedron, Hexahedron, Octahedron, Dodecahedron, Icosahedron) bounded by Euler's characteristic V - E + F = 2.",
        intuition = if (isSinhala)
          "ස්වභාවධර්මයේ ප්‍රශස්ත ශක්ති බෙදාහැරීමේ ඇල්ගොරිතමය: සූරියකාන්ත ඇට සර්පිලයේ සිට මන්දාකිණි සර්පිලය දක්වා එකම ජ්‍යාමිතික නීතිය ක්‍රියාත්මක වේ."
        else
          "The packing rules of the universe: nature’s optimal energy distribution algorithm from sunflower seed spirals to spiral galaxies.",
        branchDomain = "Universal Geometry",
        crossLinkTarget = "The Quadrivium",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, why can there only be exactly 5 convex regular polyhedra in three-dimensional Euclidean space?",
        challengeExpectedKey = "Sum of interior polygon angles meeting at any vertex must be strictly < 360°, satisfying (p-2)(q-2) < 4.",
        language = if (isSinhala) "si" else "en"
      ),

      // 4. Numerology & Archetypal Principles
      FeedCard(
        id = "master_04_numerology",
        title = if (isSinhala) "Numerology & Archetypal Principles / සංඛ්‍යා න්‍යාය" else "Numerology & Archetypal Principles",
        itch = if (isSinhala)
          "සංඛ්‍යා හුදු ප්‍රමාණ මනින දර්ශක නොවේ; සම්භාව්‍ය ඔන්ටෝලොජියේ ඒවා යථාර්ථයේ ගුණාත්මක අවධීන් නියෝජනය කරයි."
        else
          "Numbers are not mere quantity counters; in classical ontology, they represent qualitative metaphysical stages of manifestation.",
        skeleton = if (isSinhala)
          "Monad (1: එකමුතුකම, මූලික ලක්ෂ්‍යය) -> Dyad (2: ධ්‍රැවීයතාවය, ප්‍රතිවිරෝධතා) -> Triad (3: සංශ්ලේෂණය, ස්ථාවර තලය) -> Tetrad (4: භෞතික ප්‍රකාශනය, අක්ෂ 4). Decad (10: සම්පූර්ණත්වය)."
        else
          "Monad (0/1: Undifferentiated Unity, source point) -> Dyad (2: Polarity, division, tension of opposites) -> Triad (3: Synthesis, mediation, stable plane) -> Tetrad (4: Physical manifestation, 4 elements/cardinal axes). Decad = completion.",
        intuition = if (isSinhala)
          "0D ලක්ෂ්‍යය (1) -> 1D රේඛාව (2) -> 2D ත්‍රිකෝණය (3) -> 3D චතුස්තලය (4)."
        else
          "Zero-dimensional point (1) -> One-dimensional line (2) -> Two-dimensional triangle (3) -> Three-dimensional tetrahedron (4).",
        branchDomain = "Classical Numerology",
        crossLinkTarget = "Hermeticism & Laws",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, how does the transition from Dyad to Triad resolve ontological cognitive dissonance?",
        challengeExpectedKey = "The third term acts as reconciling mediator between dualistic thesis and antithesis.",
        language = if (isSinhala) "si" else "en"
      ),

      // 5. Gematria & Alphanumeric Encoding
      FeedCard(
        id = "master_05_gematria",
        title = if (isSinhala) "Gematria & Alphanumeric Encoding / ජෙමැට්‍රියා" else "Gematria & Alphanumeric Encoding",
        itch = if (isSinhala)
          "පුරාණ හෙබ්‍රෙව් සහ ග්‍රීක භාෂාවල අකුරු සහ සංඛ්‍යා එක හා සමාන විය; වචනයක් ලිවීම යනු ගණිතමය දෛශිකයක් නිර්මාණය කිරීමකි."
        else
          "In ancient Semitic and Hellenic systems, numbers and alphabets were identical; writing a word mathematically synthesized a numerical vector.",
        skeleton = if (isSinhala)
          "Hebrew Mispar Hechrachi / Greek Isopsephy: සෑම අකුරකටම නිශ්චිත සංඛ්‍යා අගයක් ඇත (Aleph/Alpha = 1, Yod/Iota = 10). සමාන එකතුවක් ඇති වචන අතර සැඟවුණු අර්ථ සම්බන්ධතා පවතී."
        else
          "Hebrew Mispar Hechrachi / Greek Isopsephy: letters assign numerical weights (Aleph/Alpha = 1, Yod/Iota = 10, Qoph/Rho = 100). Words sharing identical sums form hidden thematic matrices and harmonic resonances.",
        intuition = if (isSinhala)
          "Cryptographic hash function එකක් වැනිය: සෑම වචනයකටම Integer Hash එකක් ලැබේ; සමාන Hash ඇති වචන අතර අභ්‍යන්තර න්‍යායික සම්බන්ධයක් ඇත."
        else
          "A cryptographic hash function: every word generates an integer hash; colliding hash values reveal underlying semantic linkages.",
        branchDomain = "Alphanumeric Systems",
        crossLinkTarget = "Etymology & Linguistic Software",
        isSaved = false,
        recallStage = 0,
        challengeScenario = "Dasun, what is the isopsephy / gematria mechanism linking 'AChD' (Echad = Unity, sum 13) and 'AHBH' (Ahavah = Love, sum 13)?",
        challengeExpectedKey = "Both sum to 13, ontologically equating Unconditional Love with Structural Unity.",
        language = if (isSinhala) "si" else "en"
      ),

      // 6. Alchemy & Psychological Transmutation
      FeedCard(
        id = "master_06_alchemy",
        title = if (isSinhala) "Alchemy & Psychological Transmutation / රස විද්‍යාව" else "Alchemy & Psychological Transmutation",
        itch = if (isSinhala)
          "ලෝහ රත්තරන් බවට පත් කිරීම යනු පිටතින් පෙනෙන ආවරණයක් පමණි; සත්‍ය අරමුණ වූයේ මිනිස් මනසේ සෙවනැල්ල (Shadow) පිරිසිදු කර පූර්ණ විඥානයක් බවට පත් කිරීමයි."
        else
          "The transmutation of base metals into gold was a chemical blind for the psychological purification of the shadow into conscious integration.",
        skeleton = if (isSinhala)
          "Magnum Opus අදියර 4: 1) Nigredo (කළු වීම: වියෝජනය, Shadow මුහුණදීම) -> 2) Albedo (සුදු වීම: පිරිසිදු වීම, Anima/Animus ඒකාබද්ධතාවය) -> 3) Citrinitas (කහ වීම: ප්‍රඥාවේ උදාව) -> 4) Rubedo (රතු වීම: නොසැලෙන පෞරුෂය - Philosopher's Stone)."
        else
          "Magnum Opus stages: 1) Nigredo (Blackening: putrefaction, shadow confrontation) -> 2) Albedo (Whitening: purification, anima/animus integration) -> 3) Citrinitas (Yellowing: solar dawn of wisdom) -> 4) Rubedo (Reddening: Philosopher's Stone, unshakeable selfhood).",
        intuition = if (isSinhala)
          "දැඩි වානේ තැනීම වැනිය: අමු යකඩ අධික උෂ්ණත්වයට ලක් කර අපද්‍රව්‍ය ඉවත් වන තුරු තලා ඔප දැමීම."
        else
          "Forging Damascus steel: heating the raw iron into molten sludge to hammer out impurities until it cuts without bending.",
        branchDomain = "Internal Alchemy",
        crossLinkTarget = "General Psychology",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, what catastrophe occurs if a mind attempts Rubedo (sovereign creation) while bypassing Nigredo (shadow disintegration)?",
        challengeExpectedKey = "Psychotic inflation; unexamined shadow complexes contaminate the conscious ego with toxic arrogance.",
        language = if (isSinhala) "si" else "en"
      ),

      // 7. The Trivium
      FeedCard(
        id = "master_07_trivium",
        title = if (isSinhala) "The Trivium: Mind's Core Operating System / ත්‍රිවිධ විද්‍යාව" else "The Trivium (Grammar, Logic, Rhetoric)",
        itch = if (isSinhala)
          "නූතන අධ්‍යාපනය උගන්වන්නේ කුමක් සිතිය යුතුද යන්නයි; ත්‍රිවිධ විද්‍යාව (Trivium) උගන්වන්නේ නිවැරදිව සිතන්නේ කෙසේද යන්නයි."
        else
          "Modern schooling teaches *what* to think rather than *how* to process information; the Trivium is the error-checking compiler of human cognition.",
        skeleton = if (isSinhala)
          "1) Grammar (ආදානය / Input: නිරවද්‍ය දත්ත, නිර්වචන) -> 2) Logic (සැකසුම / Processing: තර්කානුකූල නොගැළපීම් සහ Fallacies ඉවත් කිරීම) -> 3) Rhetoric (ප්‍රතිදානය / Output: සත්‍යය පැහැදිලිව සන්නිවේදනය කිරීම)."
        else
          "Trivium triad: 1) Grammar (Input / Who, What, Where: raw data validation, terminology definitions) -> 2) Logic (Processing / Why: identifying non-contradiction, removing fallacies) -> 3) Rhetoric (Output / How: eloquent communication of truth).",
        intuition = if (isSinhala)
          "Input -> CPU processing without syntax bugs -> Output: ඕනෑම ශක්තිමත් පරිගණක ප්‍රොසෙසරයක මූලික ආකෘතිය."
        else
          "Input -> CPU processing without syntax bugs -> Output: the three-stage architecture of every robust processor.",
        branchDomain = "Classical Philosophy",
        crossLinkTarget = "The Quadrivium",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, what happens to political rhetoric when Grammar and Logic stages are completely corrupted or omitted?",
        challengeExpectedKey = "Rhetoric deteriorates into coercive sophistry, gaslighting, and pure emotional demagoguery.",
        language = if (isSinhala) "si" else "en"
      ),

      // 8. The Quadrivium
      FeedCard(
        id = "master_08_quadrivium",
        title = if (isSinhala) "The Quadrivium: The Four Mathematical Arts / චතුර්විධ විද්‍යාව" else "The Quadrivium (Arithmetic, Geometry, Music, Astronomy)",
        itch = if (isSinhala)
          "ගණිතය යනු හුදු සංඛ්‍යා එකතු කිරීමක් නොවේ; එය සංඛ්‍යාව භෞතික යථාර්ථය තුළ ප්‍රකාශ වන ආකාරය අධ්‍යයනය කිරීමයි."
        else
          "Mathematics is not arithmetic calculation; it is the study of how number unfolds into sensory physical reality.",
        skeleton = if (isSinhala)
          "1) Arithmetic (අමූර්ත සංඛ්‍යා) -> 2) Geometry (අවකාශයේ සංඛ්‍යා: හැඩතල) -> 3) Music / Harmonics (කාලය තුළ සංඛ්‍යා: සංඛ්‍යාත අනුපාත) -> 4) Astronomy (අවකාශය සහ කාලය තුළ සංඛ්‍යා: ආකාශ වස්තු චලනය)."
        else
          "1) Arithmetic (Pure Number in abstract) -> 2) Geometry (Number in Space: stationary magnitude) -> 3) Music / Harmonics (Number in Time: frequency ratios) -> 4) Astronomy / Cosmology (Number in Space and Time: celestial mechanics).",
        intuition = if (isSinhala)
          "ලක්ෂ්‍යයක් (Arithmetic) රේඛාවක්/හැඩයක් වේ (Geometry), රිද්මයට කම්පනය වේ (Music), සහ අභ්‍යවකාශයේ කක්ෂගත වේ (Astronomy)."
        else
          "A point (Arithmetic) becomes a line/shape (Geometry), pulses in rhythm (Music), and orbits across the sky (Astronomy).",
        branchDomain = "Classical Quadrivium",
        crossLinkTarget = "Music Theory & Acoustic Harmonics",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, which Quadrivium branch directly bridges Geometry and Music through proportional intervals?",
        challengeExpectedKey = "Harmonics: vibrating string lengths translate geometric proportions into audible acoustic frequencies.",
        language = if (isSinhala) "si" else "en"
      ),

      // 9. Freemasonry & Architectural Allegory
      FeedCard(
        id = "master_09_freemasonry",
        title = if (isSinhala) "Freemasonry & Architectural Allegory / වාස්තු විද්‍යාත්මක උපමාව" else "Freemasonry & Architectural Allegory",
        itch = if (isSinhala)
          "ගල් කැටයම් ශිල්පය සංකේතයක් ලෙස භාවිතා කළේ නොසැලෙන, සදාචාරාත්මක මානසික චරිතයක් ගොඩනැගීමේ රහස්‍ය විද්‍යාව ආරක්ෂා කිරීමටයි."
        else
          "Stone masonry was preserved as an operative veil for the speculative building of an upright, unshakeable psychological character.",
        skeleton = if (isSinhala)
          "Rough Ashlar (අමු ගල: නොහික්මුණු මනස) -> Conscience නමැති මිටියෙන් තලා -> Perfect Ashlar (හික්මුණු බුද්ධිය) බවට පත් කර විශ්ව දේවාලයට සවි කිරීම. Square (සදාචාරය), Compass (ආත්ම දමනය), Plumb line (සෘජු අවංකභාවය)."
        else
          "The Rough Ashlar (unpolished natural mind) chiseled by the Gavel of conscience into the Perfect Ashlar (disciplined intellect) to fit into the Great Architect's temple. Square (morality/matter), Compasses (spirit/boundary limits), Plumb line (rectitude of life).",
        intuition = if (isSinhala)
          "ඔබ එකවරම අමු ගලද, මිටිය අතැති ගල්වඩුවාද, සහ ගොඩනැගෙන දේවාලයද වේ."
        else
          "You are simultaneously the rough boulder, the stonemason with the chisel, and the temple being erected.",
        branchDomain = "Architectural Esotericism",
        crossLinkTarget = "Metaphysics & Ontology",
        isSaved = false,
        recallStage = 0,
        challengeScenario = "Dasun, in masonic allegory, what does the Plumb Line measure that the Square cannot?",
        challengeExpectedKey = "Absolute vertical rectitude and upright integrity against the gravitational pull of moral corruption.",
        language = if (isSinhala) "si" else "en"
      ),

      // 10. Hermeticism & Universal Laws
      FeedCard(
        id = "master_10_hermeticism",
        title = if (isSinhala) "Hermeticism & Universal Laws / හර්මෙටික් නීති" else "Hermeticism & Universal Laws (Mentalism, Polarity, Rhythm)",
        itch = if (isSinhala)
          "විශ්වය ක්‍රියාත්මක වන්නේ Kybalion හි දක්වා ඇති මූලධර්ම 7 මතය; ඒවා උල්ලංඝනය කළහොත් මානසික පීඩාව ගණිතමය වශයෙන් සහතික වේ."
        else
          "The universe operates on 7 immutable principles codified in the Emerald Tablet and Kybalion; violate them and psychological suffering is mathematically guaranteed.",
        skeleton = if (isSinhala)
          "1) Mentalism (සියල්ල මනසයි) -> 2) Correspondence (ඉහළ යම් සේද, පහළද එසේමය) -> 3) Vibration (කිසිවක් නිශ්චල නොවේ) -> 4) Polarity (සියල්ල ධ්‍රැවීයයි) -> 5) Rhythm (පෙන්ඩුලම චලනය වේ) -> 6) Cause & Effect (හේතුව සහ ඵලය) -> 7) Gender (උත්පාදනයට ලිංගික මූලධර්ම දෙකම අවශ්‍යයි)."
        else
          "1) Mentalism (The All is Mind) -> 2) Correspondence (As above, so below) -> 3) Vibration (Nothing rests) -> 4) Polarity (Everything is dual, extremes touch) -> 5) Rhythm (The pendulum swings) -> 6) Cause and Effect (No chance, every effect has cause) -> 7) Gender (Generation requires masculine/feminine principles).",
        intuition = if (isSinhala)
          "විඥානයේ භෞතික විද්‍යා නීති: ධ්‍රැවීයතා නීතිය (Polarity) තේරුම් ගැනීමෙන් අක්ෂය සමඟ සටන් නොකර ඔබේ මානසික තත්වය වෙනස් කළ හැකිය."
        else
          "Universal physics laws of consciousness: understanding Polarity allows you to change your mental degree without fighting the axis.",
        branchDomain = "Hermetic Science",
        crossLinkTarget = "Metaphysics & Ontology",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, how does a Master use the Principle of Rhythm to avoid emotional depression after an immense high?",
        challengeExpectedKey = "Law of Neutralization: refusing to identify with the backward swing of the mental pendulum.",
        language = if (isSinhala) "si" else "en"
      ),

      // 11. Metaphysics & Ontology
      FeedCard(
        id = "master_11_metaphysics",
        title = if (isSinhala) "Metaphysics & Ontology / පැවැත්ම පිළිබඳ විද්‍යාව" else "Metaphysics & Ontology (The Nature of Being)",
        itch = if (isSinhala)
          "භෞතික විද්‍යාව අංශු අන්තර්ක්‍රියා කරන ආකාරය පැහැදිලි කරයි, නමුත් 'පැවැත්ම' (Being) යනු කුමක්දැයි පැහැදිලි කළ නොහැක."
        else
          "Physics explains *how* particles interact, but cannot answer *what* Being itself is or why there is something rather than nothing.",
        skeleton = if (isSinhala)
          "Essence (එහි ස්වභාවය / Quiddity) vs Existence (එහි පැවැත්ම). Aristotle ගේ හේතු 4 (ද්‍රව්‍යමය, ආකෘතික, කාර්යක්ෂම, අවසාන). Spinoza ගේ තනි Substance සංකල්පය vs Kant ගේ Noumenon (වස්තුවේ සැබෑ ස්වභාවය) vs Phenomenon."
        else
          "Ontological inquiry: Essence (Quiddity / what it is) vs Existence (An-ness / that it is). Aristotle's 4 causes (Material, Formal, Efficient, Final). Spinoza's single Substance (Substantia) vs Kant's Noumenon (Thing-in-itself) vs Phenomenon.",
        intuition = if (isSinhala)
          "සිනමා තිරයක් දෙස බැලීම වැනිය: භෞතික විද්‍යාව විදුලි බුබුළු දැල්වෙන ආකාරය විස්තර කරයි; ඔන්ටෝලොජිය එම දැල්වීමට ඉඩ දෙන සුදු තිරය අධ්‍යයනය කරයි."
        else
          "Looking at a movie screen: physics describes the light pixels flickering, ontology examines the white screen that makes flickering possible.",
        branchDomain = "Metaphysics & Ontology",
        crossLinkTarget = "Hermeticism & Universal Laws",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, why does Kant claim you can never directly perceive the 'Noumenon' of an object?",
        challengeExpectedKey = "Perception is filtered through the human mind's hardcoded cognitive categories of Space and Time.",
        language = if (isSinhala) "si" else "en"
      ),

      // 12. Music Theory & Acoustic Harmonics
      FeedCard(
        id = "master_12_music_theory",
        title = if (isSinhala) "Music Theory & Acoustic Harmonics / ශබ්ද තරංග න්‍යාය" else "Music Theory & Acoustic Harmonics",
        itch = if (isSinhala)
          "සංගීතයේ Swara සහ Chords අහඹු රසාස්වාදයන් නොවේ; ඒවා සරල පූර්ණ සංඛ්‍යා අනුපාත මත පදනම් වූ භෞතික තරංග රටා වේ."
        else
          "Chords are not arbitrary aesthetic choices; they are physical standing wave interference patterns governed by simple whole-number ratios.",
        skeleton = if (isSinhala)
          "Octave = 2:1 අනුපාතය. Perfect 5th = 3:2. Perfect 4th = 4:3. Harmonic Series: fundamental frequency f_0 -> 2f, 3f, 4f... Consonance = සරල අනුපාත; Dissonance = සංකීර්ණ තරංග ගැටුම්."
        else
          "Octave = 2:1 ratio. Perfect 5th = 3:2 ratio. Perfect 4th = 4:3 ratio. Harmonic Series: fundamental f_0 generates overtones (2f, 3f, 4f, 5f...). Consonance = low integer ratio phase alignment; Dissonance = complex beat frequencies caused by wave collisions.",
        intuition = if (isSinhala)
          "ගියර් රෝද දෙකක් වැනිය: 2:1 ගියර් එකක් ගැටීමකින් තොරව කැරකෙයි; 11:7 ගියර් එකක් ගැටෙමින් ශබ්ද නගයි."
        else
          "Two gears: a 2:1 gear ratio turns smoothly without vibration; an 11:7 gear ratio rattles and grinds its teeth.",
        branchDomain = "Acoustics & Harmonics",
        crossLinkTarget = "The Quadrivium",
        isSaved = false,
        recallStage = 0,
        challengeScenario = "Dasun, why does the Pythagorean Comma (531441:524288) make pure tuning impossible across all 12 keys simultaneously?",
        challengeExpectedKey = "Twelve 3:2 fifths do not equal seven 2:1 octaves; Equal Temperament sacrifices pure intervals for uniform modulation.",
        language = if (isSinhala) "si" else "en"
      ),

      // 13. Colour Theory & Electromagnetic Wave Spectra
      FeedCard(
        id = "master_13_colour_theory",
        title = if (isSinhala) "Colour Theory & Wave Spectra / වර්ණ න්‍යාය" else "Colour Theory & Electromagnetic Wave Spectra",
        itch = if (isSinhala)
          "වර්ණය යනු භෞතික ෆෝටෝනවල පවතින සත්‍යයක් නොවේ; එය මිනිස් දෘෂ්ටි විතානයේ සෛල මඟින් මොළය තුළ නිර්මාණය කරන සංජානන මායාවකි."
        else
          "Color does not exist objectively in physical photons; it is an internal psychological perceptual illusion generated by photoreceptor ratios.",
        skeleton = if (isSinhala)
          "දෘශ්‍ය වර්ණාවලිය: 380nm (දම්) සිට 750nm (රතු). Trichromatic theory: S (නිල්, 420nm), M (කොළ, 530nm), L (රතු, 560nm) cone සෛල. Opponent Process Theory. Goethe ගේ වර්ණ සෙවනැලි සංසිද්ධිය."
        else
          "Visible spectrum: 380nm (Violet, ~790 THz) to 750nm (Red, ~400 THz). Trichromatic theory: S (Blue, 420nm), M (Green, 530nm), L (Red, 560nm) cone opsins. Opponent process theory (Red-Green, Blue-Yellow, Black-White). Goethe's reciprocal shadow phenomenon.",
        intuition = if (isSinhala)
          "ඇන්ටනා 3ක් ඇති රේඩියෝවක් වැනිය: සංඥාවට වර්ණයක් නැත; මොළය ඒ ඒ ඇන්ටනාවලට ලැබෙන සංඥා ශක්තිය අනුව වර්ණ නාම ලබා දෙයි."
        else
          "A radio receiver with 3 antennas: the station doesn't have 'color', your brain assigns color tags to the ratio of signal strength received.",
        branchDomain = "Electromagnetic Physics",
        crossLinkTarget = "Visual Composition & Spatial Balance",
        isSaved = false,
        recallStage = 0,
        challengeScenario = "Dasun, why does magenta not exist on the rainbow spectrum of electromagnetic light?",
        challengeExpectedKey = "Magenta is an extra-spectral color fabricated by the brain when S and L cones fire simultaneously without M cone activation.",
        language = if (isSinhala) "si" else "en"
      ),

      // 14. Visual Composition & Spatial Balance
      FeedCard(
        id = "master_14_visual_composition",
        title = if (isSinhala) "Visual Composition & Spatial Balance / දෘශ්‍ය සංයුතිය" else "Visual Composition & Spatial Balance",
        itch = if (isSinhala)
          "කැන්වසයක් යනු හිස් තලයක් නොවේ; මිනිස් ඇස දෘශ්‍ය අවකාශය කියවන්නේ ගුරුත්වාකර්ෂණය සහ කියවීමේ දිශානතියට අනුවය."
        else
          "A canvas is not a neutral plane; human eyes scan visual space according to hardwired gravitational and optical reading vectors.",
        skeleton = if (isSinhala)
          "Dynamic Symmetry (Root-2, Root-3, Golden Root-ϕ armatures). Gestalt නීති: Proximity, Similarity, Continuity, Closure. Visual weight: අඳුරු, අධික contrast ඇති මූලද්‍රව්‍යවලට වැඩි බරක් දැනේ."
        else
          "Dynamic Symmetry (Root-2, Root-3, Golden Root-ϕ armatures). Gestalt Laws: Proximity, Similarity, Continuity, Closure, Figure-Ground. Visual weight: darker, high-contrast, or bottom-right elements exert greater perceived psychological mass.",
        intuition = if (isSinhala)
          "යාන්ත්‍රික තරාදියක් වැනිය: වම්පස ඉහළ අඳුරු කොටුවක් තැබුවහොත්, දකුණු පසින් සමබර නොකළහොත් නරඹන්නාට අපහසුවක් දැනේ."
        else
          "A mechanical balance scale: placing a bold dark square in the top left requires counter-balancing elements or the viewer feels subconscious vertigo.",
        branchDomain = "Visual Arts & Optics",
        crossLinkTarget = "Colour Theory & Electromagnetic Wave Spectra",
        isSaved = true,
        recallStage = 1,
        challengeScenario = "Dasun, in Renaissance composition, how did masters prevent a viewer's eye from leaving the frame?",
        challengeExpectedKey = "Using opposing gaze lines, architectural diagonals, and internal frame boundaries to recycle visual attention.",
        language = if (isSinhala) "si" else "en"
      ),

      // 15. General Psychology & Cognitive Science
      FeedCard(
        id = "master_15_general_psychology",
        title = if (isSinhala) "General Psychology & Cognitive Limits / මනෝ විද්‍යාව" else "General Psychology & Cognitive Science",
        itch = if (isSinhala)
          "ඔබේ සවිඥානක මනස ඔබේ හැසිරීම්වල ප්‍රධානියා නොවේ; එය ස්වයංක්‍රීය උපවිඥානික ක්‍රියාකාරකම්වලට පසුව හේතු සාධාරණීකරණය කරන ලේකම්වරයෙකි."
        else
          "Your conscious mind is not the CEO of your behavior; it is a press secretary rationalizing automated subconscious heuristics after the fact.",
        skeleton = if (isSinhala)
          "Kahneman's System 1 (වේගවත්, අවම ශක්තිය) vs System 2 (මන්දගාමී, විග්‍රහාත්මක). Working memory සීමාව: Miller ගේ 7±2 / Cowan ගේ 4 chunks. LTP: එකට ක්‍රියාත්මක වන නියුරෝන එකට බැඳේ (NMDA Ca2+)."
        else
          "Kahneman's System 1 (Fast, associative, low-energy) vs System 2 (Slow, analytical, glucose-hungry). Working memory limit: Miller's 7±2 / Cowan's 4 chunks. LTP (Long-Term Potentiation): neurons that fire together wire together via NMDA receptor Ca2+ influx.",
        intuition = if (isSinhala)
          "ටොන් 6ක අලියෙකු (උපවිඥානය) පිට නැඟි කුඩා ඇත්ගොව්වෙකු (සවිඥානය) වැනිය: ඇත්ගොව්වාට මග පෙන්විය හැක, නමුත් අලියා බිය වුවහොත් ඇත්ගොව්වා අසරණ වේ."
        else
          "A tiny rider (conscious will) on a 6-ton elephant (subconscious instincts): the rider can nudge the path, but if the elephant panics, the rider is helpless.",
        branchDomain = "Cognitive Science",
        crossLinkTarget = "Dark Psychology, Manipulation & Defense Mechanics",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, when cognitive load saturates working memory, which decision system automatically hijacks behavioral control?",
        challengeExpectedKey = "System 1 defaults to evolutionary survival heuristics and confirmation bias.",
        language = if (isSinhala) "si" else "en"
      ),

      // 16. Dark Psychology, Manipulation & Defense Mechanics
      FeedCard(
        id = "master_16_dark_psychology",
        title = if (isSinhala) "Dark Psychology & Defense Mechanics / මානසික ආරක්ෂාව" else "Dark Psychology, Manipulation & Defense Mechanics",
        itch = if (isSinhala)
          "මනෝවිද්‍යාත්මක හසුරුවන්නන් (Manipulators) ඔබේ බුද්ධිය සමඟ තර්ක නොකරයි; ඔවුන් ඔබේ ස්නායු රසායනික දුර්වලතා (වරදකාරී හැඟීම, අස්ථිර ප්‍රතිලාභ) Hack කරයි."
        else
          "Predatory manipulators do not convince your intellect; they hack your neurochemical vulnerabilities (guilt, intermittent reinforcement, social proof).",
        skeleton = if (isSinhala)
          "The Dark Triad (Machiavellianism, Narcissism, Psychopathy). උපක්‍රම: Gaslighting (යථාර්ථය පිළිබඳ සැකය ඇති කිරීම), Love-bombing -> Devaluation චක්‍රය (Dopamine ඇබ්බැහි වීම). ආරක්ෂාව: Grey Rock ක්‍රමවේදය, Radical Boundaries, Frame Control."
        else
          "The Dark Triad (Machiavellianism, Subclinical Narcissism, Subclinical Psychopathy). Tactics: Gaslighting (epistemic destabilization), Love-bombing -> Devaluation cycle (dopamine addiction via variable rewards), Double-binds. Defense: Grey Rock technique, frame control, radical boundaries.",
        intuition = if (isSinhala)
          "Malware ප්‍රහාරයක් වැනිය: Phishing ඊමේල් RSA Encryption කඩන්නේ නැත, ඒවා පරිශීලකයාගේ හදිසිභාවය සහ ආචාරශීලී බව ප්‍රයෝජනයට ගනී."
        else
          "A computer malware attack: phishing emails don't break RSA encryption, they exploit the human user's urgency and social politeness.",
        branchDomain = "Behavioral Defense",
        crossLinkTarget = "General Psychology & Cognitive Science",
        isSaved = true,
        recallStage = 2,
        challengeScenario = "Dasun, what is the most impenetrable defensive countermeasure against a covert narcissist executing a baiting provocation?",
        challengeExpectedKey = "Grey Rock: emotional zero-reactivity denying dopamine supply, rendering you worthless to their extraction loop.",
        language = if (isSinhala) "si" else "en"
      )
    )
  }
}
