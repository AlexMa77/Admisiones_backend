package com.gestion.educativa.ui.matricula;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.MatriculaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class MatriculaViewModel_Factory implements Factory<MatriculaViewModel> {
  private final Provider<MatriculaRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public MatriculaViewModel_Factory(Provider<MatriculaRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public MatriculaViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static MatriculaViewModel_Factory create(Provider<MatriculaRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new MatriculaViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static MatriculaViewModel newInstance(MatriculaRepository repository,
      TokenManager tokenManager) {
    return new MatriculaViewModel(repository, tokenManager);
  }
}
