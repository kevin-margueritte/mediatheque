package services

import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

import models.genre.Genre
import models.movie.Movie
import models.nationality.Nationality
import repositories.{GenreRepository, MovieGenreRepository, MovieRepository, NationalityRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Classe contenant toutes les injections vers les repositories
  */
case class Mediatheque @Inject()(nationalityRepo : NationalityRepository, movieRepo : MovieRepository,
                             genreRepo : GenreRepository, movieGenreRepo : MovieGenreRepository)(implicit ec: ExecutionContext) extends IMediatheque {

  override def getCountry(country: String): Future[Option[Nationality]] = nationalityRepo.get(country)

  override def createMovie(title : String, country : String, year : Int, originalTitle : String,
                  frenchRelease : Option[String], synopsis : Option[String], note : Int) : Future[Movie] = {
    val dateFormated = frenchRelease match {
      case Some(dateString) => Some(formatDate(dateString))
      case _ => None
    }
    movieRepo.insert(originalTitle, synopsis, year, title, dateFormated, note, country)
  }

  override def movieExist(originalTitle : String) : Future[Boolean] = movieRepo.exist(originalTitle)

  override def countryExist(country : String) : Future[Boolean] = nationalityRepo.exist(country)

  override def createGenre(genres : Seq[String]) : Future[Seq[Genre]] = {
    // On souhaite sequencer, aucune utilite de traiter les Futures les uns a la suite des autres
    Future.sequence(genres.map{genre =>
      genreRepo.insert(genre)
    })
  }

  override def createAssociations(originalTitle : String, listGenres : Seq[String]) = {
    Future.sequence(listGenres.map { genre =>
      movieGenreRepo.insert(originalTitle, genre)
    })
  }

  override def filterByGenre(genre : Option[String]) : Future[Seq[(Movie, Seq[String])]] = {
    // Impossible de laisser le moteur SQL sort car, le regroupement par clé eclate le trie
    movieRepo.filterByGenreSortByYearByAndOriginalTitle(genre).map { list =>
      list.groupBy(_._1).mapValues(_.map(_._2)).toSeq.sortBy(_._1.originalTitle).sortBy(_._1.year)
    }
  }

  override def addCountryCode(country: String): Future[Nationality] = {
    nationalityRepo.insert(country.toUpperCase)
  }

  override def getAllCountryCode(): Future[Seq[Nationality]] = nationalityRepo.getAll()

  override def countMoviesByRealisationYear(year: Int): Future[Int] = movieRepo.countMoviesByRealisationYear(year)

  private def formatDate(date : String) : Timestamp = {
      val format = new SimpleDateFormat("yyyy/MM/dd")
      new Timestamp(format.parse(date).getTime)
  }

}
