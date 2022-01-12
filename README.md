# Fort Bouyard

## Présentation de Fort Bouyard : 
Fort Bouyard est un jeu ludo-pédagogique déstiné aux enfants de primaire.  
Il se joue par équipe, **(d'au moins 2)**, le but étant de valider le plus d'épreuves à chaque étape de la partie afin de valider les étapes / gagner des bonus. 
 
La première partie du jeu est de **collecter des clefs**, en cas d'echec à une épreuve, l'élève jouant cette dernière est **envoyé en prison** et l'équipe ne récupère pas la clef.  
Une fois les épreuves de clefs terminées, les joueurs peuvent au maximum **récuperer 2 clefs** et **libérer le prisonnier gagnant l'épreuve** dans des épreuves de "jugement".  
*Ces épreveuves ne sont pas joués si toutes les clefs ont été collectés.*
 
Par la suite, l'équipe doit jouer une série d'épreuve, afin de récuperer des indices pour trouver le mot code final et dévérouiller la salle au trésor.  
Il n'y a **pas de minimum d'indices**, 2 indices sont donné gratuitement et peuvent suffire à trouver le mot code. Les indices trouvés lors des épreuves sont bonus. 
 
Avant la salle au trésor se disputera la salle du conseil, ou l'équipe devra gagner des épreuves contre les Maîtres du conseil afin de gagner du temps bonus dans la salle au trésor.  
Une fois le conseil terminé, les joueurs sont envoyé dans la salle au trésor ou ils doivent trouver le mot code pour accéder à la salle au trésor.  
Dans cette dernière, un membre de l'équipe devra récuperer un maximum de pièces afin d'augmenter son score final. 
 
Enfin, le score final est calculé selon une formule incluant des multiplicateurs sur de nombreuses statistiques de l'équipe durant la partie (comme les mauvaises réponses par exemple).   
En plus de devoir terminer la partie, les élèves doivent donc réaliser une partie la plus propre possible pour augmenter leur score final.  
Ce score est sauvegardé dans un tableau des scores que les élèves peuvent consulter dans le menu principal au lancement du jeu.

## Utilisation de Fort Bouyard :

Pour une meilleure expérience, nous vous conseillons de lancer le terminal en pleine écran et également effectuer un zoom avant. 

Afin de lancer le jeu, il faut dans un premier temps le compiler (si ce n'est pas déjà fait). 
Poour ceci entrez la commande suivante dans le terminal à la racine du projet :
`./compile.sh`

Une fois le jeu compilé, il suffit de le lancer en exécutant la commande suivante, toujours à la racine du projet :
`./run.sh Main`

## Modification des épreuves :

### Modification du quiz :
* Se rendre dans le fichier **"ressources/csv/charades.csv"**
* Ajouter une ligne avec la nouvelle question en suivant le format de la première ligne du fichier (1 ligne = 1 charade)

### Modification du SoundGame :
* Se rendre dans le dossier **"ressources/sounds"**
* Y ajouter le nouveau son au format mp3
* Se rendre dans le fichier **"ressources/csv/soundgame.csv"**
* Ajouter une ligne avec le nouveau son en suivant le format de la première ligne du fichier (1 ligne = 1 son)

### Modification du Mathematix :
* Se rendre dans le dossier **"ressources/csv/mathematix.csv"**
* Ajouter une ligne avec la nouvelle question en suivant le format de la première ligne du fichier (1 ligne = 1 calcul)

### Modification du EnglishGame :
* Se rendre dans le dossier **"ressources/csv/englishgame.csv"**
* Ajouter une ligne avec la nouvelle question en suivant le format de la première ligne du fichier (1 ligne = 1 question)

### Modification du GeographyGame :
* Se rendre dans le dossier **"ressources/geography"**
* Y ajouter la nouvelle carte au format .txt en suivant l'exemple des cartes existantes.
* Se rendre dans le fichier **"ressources/csv/geography.csv"**
* Ajouter une ligne avec les nouvelles réponses en suivant le format de la première ligne du fichier (1 ligne = 1 carte)

### La modification des jeux suivants est impossible :
* Fakir
* PileOuFace
* Roulette
* Mastermind
* Shifumi
* MemoGame
* BouyardGame
- Cependant, ces jeux possèdent des **initialisations & exécutions aléatoire** les rendant "unique" à chaque nouvelle partie.
* PipeGame

## Auteurs :
* DASSONVILE Valentin (IUT A Lille - BUT 1 Groupe C)
* ZAIDI Mehdi (IUT A Lille - BUT 1 Groupe C)

#### Version : 0.24 - 12/01/2022 (Version finale)
[Repo github public](https://github.com/Hidoyatmz/SAE102PUBLIC)