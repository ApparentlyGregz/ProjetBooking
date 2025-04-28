-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 27 avr. 2025 à 13:33
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `booking`
--

-- --------------------------------------------------------

--
-- Structure de la table `adresse`
--

DROP TABLE IF EXISTS `adresse`;
CREATE TABLE IF NOT EXISTS `adresse` (
  `logement_id` int NOT NULL,
  `rue` varchar(255) NOT NULL,
  `ville` varchar(100) NOT NULL,
  `code_postal` varchar(20) NOT NULL,
  `pays` varchar(100) NOT NULL,
  `distance_centre` int DEFAULT NULL,
  PRIMARY KEY (`logement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `adresse`
--

INSERT INTO `adresse` (`logement_id`, `rue`, `ville`, `code_postal`, `pays`, `distance_centre`) VALUES
(1, '1 rue du Paradis', 'Clamart', '06000', 'France', 500),
(2, '12 avenue des Lilas', 'Paris', '75010', 'France', 100),
(3, 'Route des neiges', 'Chamonix', '74400', 'France', 2000),
(4, 'Test Rue', 'TestVille', '00000', 'France', 150);

-- --------------------------------------------------------

--
-- Structure de la table `avis`
--

DROP TABLE IF EXISTS `avis`;
CREATE TABLE IF NOT EXISTS `avis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logement_id` int NOT NULL,
  `utilisateur_id` int NOT NULL,
  `reservation_id` int NOT NULL,
  `note` int NOT NULL,
  `commentaire` text,
  `date_avis` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `reservation_id` (`reservation_id`),
  KEY `logement_id` (`logement_id`),
  KEY `utilisateur_id` (`utilisateur_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `avis`
--

INSERT INTO `avis` (`id`, `logement_id`, `utilisateur_id`, `reservation_id`, `note`, `commentaire`, `date_avis`) VALUES
(1, 1, 2, 1, 5, 'Super séjour, villa magnifique !', '2025-04-15 11:59:59'),
(2, 3, 3, 2, 4, 'Très bon séjour, un peu froid la nuit.', '2025-04-15 11:59:59');

-- --------------------------------------------------------

--
-- Structure de la table `logement`
--

DROP TABLE IF EXISTS `logement`;
CREATE TABLE IF NOT EXISTS `logement` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `type` varchar(50) NOT NULL,
  `nb_personnes_max` int NOT NULL,
  `superficie` int DEFAULT NULL,
  `nombre_etoiles` int DEFAULT NULL,
  `wifi` tinyint(1) DEFAULT '0',
  `clim` tinyint(1) DEFAULT '0',
  `parking` tinyint(1) DEFAULT '0',
  `description` text,
  `date_creation` date DEFAULT NULL,
  `nb_chambres` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `logement`
--

INSERT INTO `logement` (`id`, `nom`, `type`, `nb_personnes_max`, `superficie`, `nombre_etoiles`, `wifi`, `clim`, `parking`, `description`, `date_creation`, `nb_chambres`) VALUES
(1, 'Villa Paradis ', 'Villa', 5, 120, 5, 1, 1, 1, 'Une magnifique villa avec vue sur mer. Avec piscine', '2024-01-15', 4),
(2, 'Studio Centre', 'Villa', 2, 30, 3, 1, 0, 0, 'Studio en plein centre-ville.', '2024-03-10', 2),
(3, 'Chalet Montagne', 'Chalet', 4, 60, 4, 0, 1, 1, 'Chalet en bois dans les Alpes.', '2023-12-05', 2),
(4, 'Test Logement', 'Appartement', 2, 40, 3, 1, 0, 1, 'Petit logement de test.', '2025-04-01', 2);

-- --------------------------------------------------------

--
-- Structure de la table `logement_image`
--

DROP TABLE IF EXISTS `logement_image`;
CREATE TABLE IF NOT EXISTS `logement_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logement_id` int NOT NULL,
  `url_image` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `logement_id` (`logement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `logement_image`
--

INSERT INTO `logement_image` (`id`, `logement_id`, `url_image`) VALUES
(1, 1, 'logement1_1.jpg'),
(2, 1, 'logement1_2.jpg'),
(3, 1, 'logement1_3.jpg'),
(4, 2, 'logement2_1.jpeg'),
(5, 2, 'logement2_2.jpeg'),
(6, 2, 'logement2_3.jpeg'),
(7, 3, 'logement3_1.jpeg'),
(8, 3, 'logement3_2.jpeg'),
(9, 3, 'logement3_3.jpeg');

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

DROP TABLE IF EXISTS `paiement`;
CREATE TABLE IF NOT EXISTS `paiement` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reservation_id` int NOT NULL,
  `montant` decimal(10,2) NOT NULL,
  `date_paiement` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `methode` enum('carte','virement','paypal') NOT NULL,
  `statut` enum('complet','partiel','en_attente') NOT NULL,
  `reference` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `reservation_id` (`reservation_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id`, `reservation_id`, `montant`, `date_paiement`, `methode`, `statut`, `reference`) VALUES
(1, 1, 1062.50, '2025-04-15 11:59:59', 'carte', 'complet', 'PAIEMENT123'),
(2, 2, 200.00, '2025-04-15 11:59:59', 'paypal', 'partiel', 'AVANCE456');

-- --------------------------------------------------------

--
-- Structure de la table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logement_id` int NOT NULL,
  `titre` varchar(100) NOT NULL,
  `description` text,
  `pourcentage` decimal(5,2) NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `pour_anciens_clients` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `logement_id` (`logement_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `promotion`
--

INSERT INTO `promotion` (`id`, `logement_id`, `titre`, `description`, `pourcentage`, `date_debut`, `date_fin`, `pour_anciens_clients`) VALUES
(1, 1, 'Promo été', 'Réduction estivale pour les familles', 15.00, '2025-06-01', '2025-08-31', 0),
(2, 3, 'Offre anciens clients', 'Pour nos fidèles clients', 10.00, '2025-01-01', '2025-03-31', 1);

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logement_id` int NOT NULL,
  `utilisateur_id` int NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `nombre_adultes` int NOT NULL DEFAULT '1',
  `nombre_enfants` int NOT NULL DEFAULT '0',
  `nombre_chambres` int NOT NULL DEFAULT '1',
  `statut` enum('confirmée','annulée','en_attente') NOT NULL DEFAULT 'confirmée',
  `prix_total` decimal(10,2) NOT NULL,
  `promotion_id` int DEFAULT NULL,
  `date_reservation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `logement_id` (`logement_id`),
  KEY `utilisateur_id` (`utilisateur_id`),
  KEY `promotion_id` (`promotion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id`, `logement_id`, `utilisateur_id`, `date_debut`, `date_fin`, `nombre_adultes`, `nombre_enfants`, `nombre_chambres`, `statut`, `prix_total`, `promotion_id`, `date_reservation`) VALUES
(1, 1, 2, '2025-07-10', '2025-07-15', 2, 2, 2, 'confirmée', 1062.50, 1, '2025-04-15 11:59:59'),
(2, 3, 3, '2025-02-10', '2025-02-15', 2, 0, 1, 'en_attente', 675.00, 2, '2025-04-15 11:59:59');

-- --------------------------------------------------------

--
-- Structure de la table `tarif`
--

DROP TABLE IF EXISTS `tarif`;
CREATE TABLE IF NOT EXISTS `tarif` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logement_id` int NOT NULL,
  `prix_nuit` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `logement_id` (`logement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `tarif`
--

INSERT INTO `tarif` (`id`, `logement_id`, `prix_nuit`) VALUES
(1, 1, 250.00),
(2, 2, 80.00),
(3, 3, 150.00);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` int NOT NULL AUTO_INCREMENT,
  `identifiant` varchar(100) NOT NULL,
  `mdp` varchar(255) NOT NULL,
  `email` varchar(191) NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `type_compte` enum('client','admin','nouveau_client') NOT NULL DEFAULT 'nouveau_client',
  `ancien_client` tinyint(1) NOT NULL DEFAULT '0',
  `date_inscription` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifiant` (`identifiant`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `identifiant`, `mdp`, `email`, `nom`, `prenom`, `telephone`, `type_compte`, `ancien_client`, `date_inscription`) VALUES
(1, 'admin1', 'adminpass', 'admin@example.com', 'Admin', 'One', '0000000000', 'admin', 0, '2025-04-15 11:59:59'),
(2, 'client1', 'pass123', 'client1@example.com', 'Dupont', 'Jean', '0601010101', 'client', 1, '2025-04-15 11:59:59'),
(3, 'client2', 'secret', 'client2@example.com', 'Martin', 'Lucie', '0602020202', 'nouveau_client', 0, '2025-04-15 11:59:59'),
(4, 'testuser', 'testpass', 'test@example.com', 'Test', 'User', '0102030405', 'client', 1, '2025-04-15 11:59:59');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `adresse`
--
ALTER TABLE `adresse`
  ADD CONSTRAINT `fk_adresse_logement` FOREIGN KEY (`logement_id`) REFERENCES `logement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `logement_image`
--
ALTER TABLE `logement_image`
  ADD CONSTRAINT `fk_image_logement` FOREIGN KEY (`logement_id`) REFERENCES `logement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `fk_reservation_logement` FOREIGN KEY (`logement_id`) REFERENCES `logement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `tarif`
--
ALTER TABLE `tarif`
  ADD CONSTRAINT `fk_tarif_logement` FOREIGN KEY (`logement_id`) REFERENCES `logement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*
eau
ordinateur
voyage
lune
bouteille
marché
bus
station
musée
maison
neige
chat
stylo
ordinateur
bus
fleur
chien
boisson
photo
bus
vélo
rêve
boulangerie
repas
théâtre
train
voyage
fleur
voiture
lait
fruit
magasin
marché
hôtel
vent
travail
forêt
théâtre
supermarché
magasin
légume
ciel
restaurant
photo
film
route
chien
galaxie
fruit
musée
café
eau
stylo
fruit
station
rêve
film
café
théâtre
voiture
école
pluie
banque
travail
restaurant
chien
moto
jus
plage
légume
vent
bouteille
café
bus
jardin
bouteille
eau
théâtre
eau
arbre
fromage
pont
film
camion
marché
montagne
théâtre
jus
vélo
lune
moto
jus
boisson
chat
camion
repas
station
hôtel
station
repas
chocolat
livre
fruit
arbre
musée
vent
repas
repas
vent
café
magasin
voyage
vent
photo
ordinateur
film
théâtre

maison
vent
musée
boisson
jardin
route
feu
glace
livre
ciel
chat
musée
ordinateur
route
chocolat
chien
pluie
légume
repas
gâteau
étoile
bus
magasin
bouteille
fleur
rivière
planète
pont
moto
photo
village
livre
film
camion
étoile
boisson
jardin
pluie
travail
feu
glace
légume
ciel
boisson
rivière
univers
vin
chat
arbre
rivière
vent
rêve
boisson
glace
ordinateur
photo
travail
galaxie
lait
fromage
boulangerie
bus
musique
arbre
voiture
boulangerie
chat
station
voiture
bière
bateau
marché
route
montagne
vélo
chocolat
repas
bière
soleil
livre
étoile
pain
légume
boulangerie
marché
ciel
lait
légume
étoile
jus
jardin
maison
travail
film
photo
train
ordinateur
gâteau
boulangerie
boulangerie
route
soleil
café
légume
rivière
livre
marché
fruit
soleil
moto
amour
vent
bouteille
bus
lait
neige
neige
jus
train
marché
vin
vin
pont
boulangerie
livre
boulangerie
boisson
école
eau
boisson
voiture
vélo
musique
ville
fleur
neige
rêve
photo
bus
voiture
restaurant
hôtel
galaxie
stylo
étoile
maison
eau
amour
vélo
vin
marché
ordinateur
vélo
restaurant
amour
musique
avion
magasin
fromage
chat
école
lune
livre
chocolat
neige
plage
station
chocolat
bus
village
photo
café
fruit
gâteau
restaurant
bus
film
fromage
ville
lait
pont
bus
forêt
jus
station
montagne
route
banque
chien
ordinateur
train
route
film
banque
banque
maison
soleil
galaxie
livre
moto
magasin
rêve
travail
plage
plage
train
vélo
banque
photo
légume
pain
chat
pluie
musique
chat
lait
station
boulangerie
banque
légume
lait
plage
photo
musique
montagne
chien
livre
voyage
musée
maison
musée
rivière
légume
glace
lait
pluie
univers
voyage
livre
bateau
chat
rêve
bière
hôtel
pluie
photo
musée
chien
forêt
forêt
musée
musée
voiture
café
vent
école
jus
musée
lait
ville
voiture
avion
fruit
café
vent
théâtre
arbre
planète
maison
univers
café
avion
bière
soleil
fromage
magasin
école
marché
ciel
fruit
stylo
théâtre
stylo
soleil
banque
amour
film
univers
rêve
gâteau
magasin
photo
gâteau
pain
théâtre
pont
vent
ciel
soleil
pont
stylo
plage
ciel
lait
jus
banque
montagne
forêt
musique
chocolat
gâteau
chien
forêt
stylo
avion
chien
fruit
ville
restaurant
voiture
fleur
supermarché
lune
hôtel
arbre
pain
jus
vélo
musique
bouteille
univers
fromage
moto
pont
jus
glace
bière
route
soleil
voiture
avion
univers
ordinateur
jardin
musique
école
galaxie
banque
moto
univers
fleur
voiture
rêve
plage
travail
plage
chien
route
gâteau
ordinateur
ordinateur
théâtre
voiture
légume
pain
avion
route
eau
vélo
lait
stylo
glace
pluie
neige
marché
chien
théâtre
feu
chat
livre
pluie
bière
fruit
ciel
bière
maison
boisson
bouteille
boisson
station
galaxie
magasin
restaurant
soleil
pain
étoile
pluie
boisson
ciel
fromage
musique
photo
stylo
maison
maison
jus
maison
stylo
galaxie
rêve
photo
ordinateur
amour
théâtre
feu
boulangerie
feu
gâteau
voiture
ciel
étoile
fruit
ciel
montagne
boisson
étoile
forêt
stylo
voiture
musée
village
pluie
station
ville
film
bouteille
magasin
bouteille
bus
pluie
pain
univers
magasin
univers
village
glace
fruit
musée
maison
boisson
vélo
café
avion
marché
vélo
avion
boisson
banque
soleil
plage
école
supermarché
galaxie
village
fromage
montagne
bus
café
vent
fromage
galaxie
banque
bus
chien
lune
amour
banque
lune
boisson
photo
fromage
rêve
travail
avion
musique
marché
bus
hôtel
magasin
pont
marché
lune
galaxie
livre
avion
pont
pluie
plage
jus
moto
voyage
pluie
pont
eau
soleil
univers
arbre
vélo
ciel
pont
ciel
chocolat
photo
chat
chocolat
feu
fleur
eau
vin
photo
musique
stylo
café
vin
vin
bateau
café
eau
étoile
plage
vélo
café
forêt
vin
route
livre
jardin
train
chien
repas
avion
galaxie
eau
musée
univers
amour
repas
arbre
chocolat
supermarché
chocolat
stylo
ciel
fruit
gâteau
avion
avion
forêt
ciel
eau
lait
musée
bouteille
galaxie
village
jardin
restaurant
amour
camion
café
étoile
soleil
galaxie
voyage
musique
voiture
jus
galaxie
légume
fleur
bouteille
rivière
musique
chocolat
chien
planète
magasin
boulangerie
avion
avion
camion
pont
boulangerie
chien
chat
livre
galaxie
musée
étoile
boisson
légume
bière
bateau
vélo
ciel
musique
marché
fromage
boisson
marché
vélo
stylo
village
banque
plage
fleur
vent
fromage
voiture
fruit
jus
rivière
fromage
lune
bus
bouteille
théâtre
feu
repas
jus
stylo
vent
bouteille
légume
camion
ordinateur
avion
supermarché
voiture
marché
vin
rivière
étoile
musée
maison
rivière
pont
neige
pain
maison
banque
film
fleur
bière
bière
maison
station
route
voiture
livre
boisson
stylo
marché
voyage
chien
chocolat
bateau
route
étoile
pain
pain
ville
hôtel
banque
feu
neige
bière
lait
travail

*/
