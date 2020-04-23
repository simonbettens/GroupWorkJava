package repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import domein.Aankondiging;
import domein.AankondigingPrioriteit;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.Media;
import domein.MediaType;
import domein.Sessie;
import domein.SessieAankondiging;
import domein.SessieGebruiker;
import domein.SessieKalender;
import domein.StatusType;
import domein.TypeMedia;

public class InitData {
	private GebruikerDaoJpa gebruikerRepo;
	private SessieKalenderDaoJpa sessieKalenderRepo;
	private SessieDaoJpa sessieRepo;
	private GenericDaoJpa<Aankondiging> aankondingRepo;
	private GenericDaoJpa<SessieGebruiker> sessiegebruikerRepo;
	private GenericDaoJpa<Media> mediaRepo;

	public InitData() {
		this.gebruikerRepo = new GebruikerDaoJpa();
		this.sessieKalenderRepo = new SessieKalenderDaoJpa();
		this.sessieRepo = new SessieDaoJpa();
		this.aankondingRepo = new GenericDaoJpa<>(Aankondiging.class);
		this.sessiegebruikerRepo = new GenericDaoJpa<>(SessieGebruiker.class);
		this.mediaRepo = new GenericDaoJpa<>(Media.class);
		//run();
		showResult();
	}

	private void showResult() {
		// TODO Auto-generated method stub
		Gebruiker simon = gebruikerRepo.getByUsername("sb123456");
		System.out.println("Gebruiker simon : " + simon.toString());
		System.out.println();
		sessieRepo.getAll().forEach(s->System.out.println(s.toString()));
		System.out.println();
		aankondingRepo.getAll().forEach(a->System.out.println(a.toString()));
		System.out.println();
		mediaRepo.getAll().forEach(m->System.out.println(m.toString()));
	}

	private void run() {
		GenericDaoJpa.startTransaction();
		LocalDateTime huidigetijd = LocalDateTime.now().plusMinutes(2);

		// gebruikers
		
		Gebruiker pieter = new Gebruiker("Pieter", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
				1125302310790L, GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		Gebruiker simon = new Gebruiker("Simon", "Bettens", "sb123456", "appeltjes", "simon.bettens@student.hogent.be",
				1103720665999L, GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		Gebruiker ruben = new Gebruiker("Ruben", "Naudts", "rn123456", "peertjes", "ruben.naudts@student.hogent.be",
				1138608425471L, GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		Gebruiker aaron = new Gebruiker("Aaron", "Sys", "as123456", "snoepjes", "aaron.sys@student.hogent.be",
				1101056340993L, GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		Gebruiker admin = new Gebruiker("Harm", "De Weirdt", "hdw123456", "adminpass", "harm.deweirdt@hogent.be",
				1103720666020L, GebruikerType.HOOFDVERANTWOORDELIJKE, StatusType.ACTIEF);
		Gebruiker verantwoordelijke = new Gebruiker("Simon", "Bettens", "sbv123456", "test",
				"simon.bettens@student.hogent.be", 1103720666030L, GebruikerType.VERANTWOORDELIJKE, StatusType.ACTIEF);

		gebruikerRepo.insert(pieter);
		gebruikerRepo.insert(simon);
		gebruikerRepo.insert(ruben);
		gebruikerRepo.insert(aaron);
		gebruikerRepo.insert(admin);
		gebruikerRepo.insert(verantwoordelijke);
		
		GenericDaoJpa.commitTransaction();
		
		// SessieKalender(LocalDate startDatum, LocalDate eindDatum)
		GenericDaoJpa.startTransaction();
		LocalDate start = LocalDate.of(2019, 9, 20);
		SessieKalender kalender2019_2020 = new SessieKalender();
		kalender2019_2020.setStartDatumInitData(start);
		kalender2019_2020.setEindDatum(start.plusDays(365));
		sessieKalenderRepo.insert(kalender2019_2020);
		GenericDaoJpa.commitTransaction();

		// sessies
		// Sessie(Gebruiker verantwoordelijke, String naam, LocalDateTime startDatum,
		// LocalDateTime eindDatum,boolean gesloten, int maxCap,String lokaal, String
		// beschrijving)
		
		GenericDaoJpa.startTransaction();
		Sessie sessie1 = new Sessie(admin, "The Three Laws of TDD (featuring Kotlin)",
				huidigetijd.plusDays(1), huidigetijd.plusDays(1).plusHours(1), false, 20,
				"GSCHB4.026",
				"Testen is moeilijk aan te brengen tijdens je opleiding want je komt toch niet vaak terug op oude code omdat de \"klant\" aanpassing wilt. Maar iedereen heeft al veel tijd verloren omdat er bugs waren, of omdat de code niet goed te lezen was. \n Maar Uncle Bob is terug, en deze keer legt hij de drie wetten van Test - Driven Development uit, en toont ze ook in actie. Dit zijn de drie regels:\n 1.You are not allowed to write any production code unless it is to make a failing unit test pass. \n 2.You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures. \n 3.You are not allowed to write any more production code than is sufficient to pass the one failing unit test. \n Door deze drie regels te volgen garandeer je dat je code altijd doet wat ze moet doen! Als je code bijschrijft of aanpast kan je altijd vertrouwen op je tests.");

		Sessie sessie2 = new Sessie(verantwoordelijke, "Life is terrible: let's talk about the web",
				huidigetijd.plusDays(2), huidigetijd.plusDays(2).plusHours(1), false, 10,
				"GSCHB4.026", "");

		Sessie sessie3 = new Sessie(admin, "TDD: Where did it all go wrong?", huidigetijd.plusDays(3), huidigetijd.plusDays(3).plusHours(1), false, 30, "GSCHB4.026",
				"In Ontwerpen 1 leerde je al over het testen van software, en hoe TDD vitaal is voor het afleveren van werkende software. En in de volgende semesters bleef die focus op het schrijven van testen aanwezig. Maar moet die focus op TDD er wel zo sterk zijn ? Is wat nuance niet aan de orde ? Ian Cooper brengt in deze talk een duidelijk antwoord op deze vraag.Hij heeft al meer dan 20 jaar ervaring en heeft vooral gewerkt aan de architectuur van grote.NET - projecten.");

		Sessie sessie4 = new Sessie(admin, "De weg naar de Cloud", huidigetijd.plusDays(4), huidigetijd.plusDays(4).plusHours(1), false, 20, "GSCHB4.026", "");

		Sessie sessie5 = new Sessie(admin, "Improving Security Is Possible?", huidigetijd.plusDays(5), huidigetijd.plusDays(5).plusHours(1), false, 20, "GSCHB4.026",
				"In deze talk geeft James Mickens van Harvard University zijn ongezouten mening over de mysteries van Machine Learning (\"The stuff is what the stuff is, brother.\") en andere \"hippe en innovatieve\" frameworks en technologieën, en hoe de focus op innovatie ervoor zorgt dat er nooit tijd is voor security.");

		Sessie bijnaGestarteSessie1 = new Sessie(admin, "Power use of Unix", huidigetijd.plusMinutes(45),
				huidigetijd.plusMinutes(75), false, 20, "GSCHB4.026",
				"Kennis van de commandline gecombineerd met de basis van reguliere expressies laten je toe om een hoger niveau van productiviteit te bereiken. Deze talk introduceert in een halfuur de meest bruikbare UNIX commando's om je workflow te optimaliseren. De perfecte sessie voor iedereen die wil kennismaken met de kracht van de commandline!");

		Sessie sessie6 = new Sessie(verantwoordelijke, "How to be a happy Developer. Now!",
				huidigetijd.plusDays(6), huidigetijd.plusDays(6).plusHours(1), false, 20,
				"GSCHB4.026",
				"Veel ontwikkelaars claimen dat ze van hun hobby hun beroep hebben gemaakt. \nDus, wat kunnen we doen om de huidige situatie te verbeteren ? Hoe kunnen we onszelf beter laten voelen ? Dieze talk richt zich op een aantal eenvoudig te implementeren tactieken die ieder van ons kan gebruiken vanaf morgen, waardoor ons leven een beetje makkelijker en leuker wordt: stuk voor stuk, dag na dag.");

		Sessie gestartSessie1 = new Sessie(admin, "How Netflix thinks of DevOps", huidigetijd,
				huidigetijd.plusMinutes(45), false, 20, "GSCHB4.026",
				"Netflix wordt gezien als een grote DevOps omgeving. Toch is “DevOps” niet iets waar ze veel over spreken. Als het dan toch zo’n kritisch deel is voor het succes van de organisatie, waarom horen we er niet meer over?\nNetflix ziet DevOps als het resultaat van een duidelijke bedrijfscultuur, niet als oplossing van een bepaald probleem. Alles begint bij de bedrijfscultuur, chaos is je vriend en vertrouwen is van absoluut belang.");

		Sessie[] sessies = { sessie1, sessie2, sessie3, sessie4, sessie5, sessie6, gestartSessie1,
				bijnaGestarteSessie1 };
		Arrays.asList(sessies).stream().forEach(s->{kalender2019_2020.addSessie(s);sessieRepo.insert(s);});
		sessieKalenderRepo.update(kalender2019_2020);
		GenericDaoJpa.commitTransaction();
		
		//inschrijvingen
		SessieGebruiker sessie1Simon = new SessieGebruiker(simon,sessie1,false,false);
		sessie1.addInschrijving(sessie1Simon);
		simon.addInschrijvingen(sessie1Simon);
		sessiegebruikerRepo.insert(sessie1Simon);
		sessieRepo.update(sessie1);
		gebruikerRepo.update(simon);
		
		
		SessieGebruiker sessie1Pieter = new SessieGebruiker(pieter,sessie1,false,false);
		sessie1.addInschrijving(sessie1Pieter);
		pieter.addInschrijvingen(sessie1Pieter);
		sessiegebruikerRepo.insert(sessie1Pieter);
		sessieRepo.update(sessie1);
		gebruikerRepo.update(pieter);
		
        SessieGebruiker sessieGebruiker1 = new SessieGebruiker(simon,sessie4,true,true );
        sessie4.addInschrijving(sessieGebruiker1);
        simon.addInschrijvingen(sessieGebruiker1);
        sessiegebruikerRepo.insert(sessieGebruiker1);
		sessieRepo.update(sessie4);
		gebruikerRepo.update(simon);
		
        SessieGebruiker sessieGebruiker2 = new SessieGebruiker(aaron, sessie5, true,true);
        sessie5.addInschrijving(sessieGebruiker2);
        aaron.addInschrijvingen(sessieGebruiker2);
        sessiegebruikerRepo.insert(sessieGebruiker2);
		sessieRepo.update(sessie5);
		gebruikerRepo.update(aaron);
        
        SessieGebruiker sessieGebruiker3 = new SessieGebruiker(pieter,sessie5,true,true );
        sessie5.addInschrijving(sessieGebruiker3);
		pieter.addInschrijvingen(sessieGebruiker3);
		sessiegebruikerRepo.insert(sessieGebruiker3);
		sessieRepo.update(sessie5);
		gebruikerRepo.update(pieter);
		
		//aankondingen
		GenericDaoJpa.startTransaction();
		Aankondiging algemeneAankonding1 = new Aankondiging(admin,huidigetijd.plusHours(1),
				"Studenten met interesse kunnen altijd een mailtje sturen en dan zend ik de filmpjes van de afgelaste sessies door",AankondigingPrioriteit.LAAG);
        Aankondiging algemeneAankonding2 = new Aankondiging(admin,huidigetijd,
        		"Bedankt aan alle studenten die feedback hebben gegeven op de sessies, deze komen goed van pas bij het kiezen van de volgende", AankondigingPrioriteit.LAAG);
        Aankondiging algemeneAankonding3 = new Aankondiging(admin,huidigetijd,
        		"Wegens de huidige coronamaatregelen worden hierbij alle sessies afgelast.",  AankondigingPrioriteit.HOOG);
        Aankondiging algemeneAankonding4 = new Aankondiging(admin,huidigetijd,
        		"Het IT-Lab zal niet open zijn tot minstens 5 april wegens de landelijke coronamaatregelen", AankondigingPrioriteit.HOOG);
        
        SessieAankondiging sessieAankonding1 = new SessieAankondiging(admin,huidigetijd, 
        		"Studenten die nog steeds interesse vertonen kunnen mij een mailtje sturen, ik zal het filmpje van de sessie doorsturen" ,
        		AankondigingPrioriteit.LAAG,sessie1); 
        sessie1.addAankondiging(sessieAankonding1);
        sessieRepo.update(sessie1);
        
        SessieAankondiging sessieAankonding2 = new SessieAankondiging(admin,huidigetijd, "Deze sessie zal afgelast worden door de huidige coronamaatregelen",
        		AankondigingPrioriteit.HOOG,sessie1);
        sessie1.addAankondiging(sessieAankonding2);
        sessieRepo.update(sessie1);
        
        SessieAankondiging sessieAankonding3 = new SessieAankondiging(admin,huidigetijd, 
        		"Deze sessie zal afgelast worden door de huidige coronamaatregelen", AankondigingPrioriteit.HOOG, sessie2);
        sessie2.addAankondiging(sessieAankonding1);
        sessieRepo.update(sessie2);
        
        Aankondiging[] aankondingen = { algemeneAankonding1, algemeneAankonding2, algemeneAankonding3, algemeneAankonding4, 
                sessieAankonding1, sessieAankonding2,sessieAankonding3 };
        Arrays.asList(aankondingen).stream().forEach(a->aankondingRepo.insert(a));
        
        GenericDaoJpa.commitTransaction();
        //media items
        GenericDaoJpa.startTransaction();
        Media link1 = new Media(sessie1,"https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd.plusHours(1), MediaType.LINK);
        sessie1.addMediaItem(link1);
        mediaRepo.insert(link1);
        
        Media video1 = new Media(sessie1,"https://www.youtube.com/embed/1Rcf8-yk6_o", "Youtbe Linux", huidigetijd.plusHours(1), MediaType.YOUTUBEVIDEO);
        Media video2 = new Media(sessie1,"/videos/testVideo.mp4", "Video binary", huidigetijd.plusHours(1), MediaType.VIDEO);

        sessie1.addMediaItem(video1);
        sessie1.addMediaItem(video2);
        Media[] videos = { video1, video2 };
        Arrays.asList(videos).stream().forEach(v->{mediaRepo.insert(v);});

        Media afbeelding1 = new Media(sessie1,"/images/StockFoto1.jpg", "StockFoto1", huidigetijd.plusHours(1), MediaType.AFBEELDING);
        Media afbeelding2 = new Media(sessie1,"/images/StockFoto2.jpg", "StockFoto2", huidigetijd.plusHours(1), MediaType.AFBEELDING);
        Media afbeelding3 = new Media(sessie1,"/images/StockFoto3.jpg", "StockFoto3", huidigetijd.plusHours(1), MediaType.AFBEELDING);
        Media afbeelding4 = new Media(sessie1,"/images/StockFoto1.jpg", "StockFoto1", huidigetijd.plusHours(1), MediaType.AFBEELDING);
        sessie1.addMediaItem(afbeelding1);
        sessie1.addMediaItem(afbeelding2);
        sessie1.addMediaItem(afbeelding3);
        sessie2.addMediaItem(afbeelding4);
        Media[] afbeeldingen = { afbeelding1, afbeelding2, afbeelding3, afbeelding4 };
        Arrays.asList(afbeeldingen).stream().forEach(a->{mediaRepo.insert(a);});
        
        Media doc1 = new Media(sessie1,"/documents/Test_document_itlab_sessie.docx", "Word document", huidigetijd.plusHours(1), MediaType.WORD);
        Media doc2 = new Media(sessie1,"/documents/Test_document_itlab_sessie.pdf", "Pdf document", huidigetijd.plusHours(1), MediaType.PDF);
        Media doc3 = new Media(sessie1,"/documents/Test_excel.xlsx", "Excel document",huidigetijd.plusHours(1), MediaType.EXCEL);
        Media doc4 = new Media(sessie1,"/documents/Test_powerpoint.pptx", "Powerpoint document", huidigetijd.plusHours(1), MediaType.POWERPOINT);
        Media doc5 = new Media(sessie1,"/documents/TestDocumenten.zip", "Gezipte map", huidigetijd.plusHours(1), MediaType.ZIP);
        sessie1.addMediaItem(doc1);
        sessie1.addMediaItem(doc2);
        sessie1.addMediaItem(doc3);
        sessie1.addMediaItem(doc4);
        sessie1.addMediaItem(doc5);
        Media[] documenten = { doc1, doc2, doc3, doc4, doc5 };
        Arrays.asList(documenten).stream().forEach(d->{mediaRepo.insert(d);});
        
        sessieRepo.update(sessie1);
        sessieRepo.update(sessie2);
        
		GenericDaoJpa.commitTransaction();
		
		 
	}

}
