package com.brunodegan.ifood_challenge.ui.screen.upComingMovies

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.brunodegan.ifood_challenge.R
import com.brunodegan.ifood_challenge.base.ui.ErrorUiState
import com.brunodegan.ifood_challenge.base.ui.LoaderUiState
import com.brunodegan.ifood_challenge.base.ui.ObserveAsEvent
import com.brunodegan.ifood_challenge.base.ui.PosterShape
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.metrics.TrackScreen
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.events.UpcomingMoviesUiEvent
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.state.UpComingMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.viewModel.UpComingMoviesViewModel
import org.koin.androidx.compose.koinViewModel

private const val SCREEN_NAME = "UpComingMoviesScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpComingMoviesScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    onShowSnackbar: (String) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpComingMoviesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateUp()
    }

    LaunchedEffect(Unit) {
        viewModel.getUpCommingMovies()
    }

    ObserveAsEvent(events = viewModel.snackbarState) { event ->
        when (event) {
            is SnackbarUiStateHolder.SnackbarUi -> {
                onShowSnackbar(event.msg)
            }
        }
    }

    UpComingMoviesScreen(
        scrollBehavior = scrollBehavior,
        state = uiState,
        onEvent = {
            viewModel.onUiEvent(event = it)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpComingMoviesScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    state: UpComingMoviesUiState,
    onEvent: (UpcomingMoviesUiEvent) -> Unit,
    modifier: Modifier,
) {
    when (state) {
        is UpComingMoviesUiState.Initial -> {
            TrackScreen(screenName = SCREEN_NAME)
        }

        is UpComingMoviesUiState.Success -> {
            SuccessState(
                scrollBehavior = scrollBehavior,
                modifier = modifier,
                viewData = state.viewData,
                onFavoriteButtonClicked = {
                    onEvent(UpcomingMoviesUiEvent.OnAddFavButtonClickedUiEvent(it))
                },
                onRemoveFavButtonClickedUiEvent = {
                    onEvent(UpcomingMoviesUiEvent.OnRemoveFavButtonClickedUiEvent(it))
                }
            )
        }

        is UpComingMoviesUiState.Error -> {
            ErrorUiState(
                modifier = modifier,
                errorData = state.error,
            ) {
                onEvent(UpcomingMoviesUiEvent.OnRetryButtonClickedUiEvent)
            }
        }

        is UpComingMoviesUiState.Loading -> {
            LoaderUiState()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    viewData: List<UpcomingMoviesEntity>,
    onFavoriteButtonClicked: (Int) -> Unit,
    onRemoveFavButtonClickedUiEvent: (Int) -> Unit,
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        items(viewData.size, key = { index -> viewData[index].title }) { position ->
            UpComingMovesCard(
                viewData = viewData[position],
                onFavoriteButtonClicked = {
                    onFavoriteButtonClicked(it)
                },
                onRemoveFavButtonClickedUiEvent = {
                    onRemoveFavButtonClickedUiEvent(it)
                }
            )
        }
    }
}

@Composable
private fun UpComingMovesCard(
    modifier: Modifier = Modifier,
    viewData: UpcomingMoviesEntity,
    onFavoriteButtonClicked: (Int) -> Unit,
    onRemoveFavButtonClickedUiEvent: (Int) -> Unit,
) {
    var isFavoriteButtonClicked by rememberSaveable { mutableStateOf(false) }

    val imageRequest = ImageRequest.Builder(LocalContext.current).data(viewData.posterPath)
        .decoderFactory(SvgDecoder.Factory()).scale(Scale.FIT).crossfade(true)
        .placeholder(R.drawable.movie_icon).error(R.drawable.error_img).build()

    val cardColor by animateColorAsState(
        targetValue = if (isFavoriteButtonClicked) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }, animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioHighBouncy,
        ), label = "animation"
    )
    Card(
        colors = CardDefaults.elevatedCardColors(cardColor),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
        border = BorderStroke(
            dimensionResource(R.dimen.card_border_elevation), MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = dimensionResource(R.dimen.card_padding))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.double_padding))
                .wrapContentSize()
        ) {
            Image(
                painter = painterResource(
                    if (isFavoriteButtonClicked) {
                        R.drawable.added_to_favorites
                    } else {
                        R.drawable.not_added_to_favorites
                    }
                ),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(R.dimen.favorite_icon_size))
                    .align(Alignment.End)
                    .padding(top = dimensionResource(R.dimen.double_padding))
                    .clickable {
                        isFavoriteButtonClicked = !isFavoriteButtonClicked
                        if (isFavoriteButtonClicked) {
                            onFavoriteButtonClicked(viewData.id)
                        } else {
                            onRemoveFavButtonClickedUiEvent(viewData.id)
                        }
                    }
            )
            AsyncImage(
                model = imageRequest,
                fallback = painterResource(R.drawable.movie_icon),
                error = painterResource(R.drawable.error_img),
                contentDescription = "",
                filterQuality = FilterQuality.Low,
                modifier = Modifier
                    .clip(PosterShape())
                    .size(
                        dimensionResource(R.dimen.movie_poster_size),
                        dimensionResource(R.dimen.movie_poster_size)
                    )
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        top = dimensionResource(R.dimen.double_padding),
                        bottom = dimensionResource(R.dimen.base_padding)
                    )
            )
            Text(
                text = viewData.title,
                fontWeight = FontWeight.W600,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.double_padding),
                    start = dimensionResource(R.dimen.base_padding)
                )
            )
            Text(
                text = viewData.overview,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.W400,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_overview_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding),
                )
            )
            Text(
                text = stringResource(R.string.movie_language).format(
                    viewData.originalLanguage.toUpperCase(Locale.current)
                ),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )
            Text(
                text = stringResource(R.string.movie_release_date).format(
                    viewData.releaseDate
                ),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )
            Text(
                text = stringResource(R.string.movie_popularity).format(
                    viewData.popularity.toString()
                ),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.movie_vote_average),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(
                        value = ResourcesCompat.getFloat(
                            LocalContext.current.resources,
                            R.dimen.movie_language_font_size
                        ), type = TextUnitType.Sp
                    ),
                    textAlign = TextAlign.Justify,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        dimensionResource(R.dimen.base_padding)
                    )
                )

                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.base_padding),
                        bottom = dimensionResource(R.dimen.double_padding)
                    )
                ) {
                    items(viewData.voteAverage.toInt()) { _ ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.movie_vote_average),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(
                                start = dimensionResource(R.dimen.small_padding)
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UpcomingScreenPreview() {
    UpComingMoviesScreen(
        state = UpComingMoviesUiState.Success(
            viewData = listOf(
                UpcomingMoviesEntity(
                    id = 0,
                    title = "title",
                    posterPath = "posterPath",
                    overview = "overview",
                    originalLanguage = "originalLanguage",
                    popularity = 10.0,
                    voteAverage = 7.5,
                    releaseDate = "24/04/2025"
                )
            )
        ),
        onEvent = {},
        modifier = Modifier,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}