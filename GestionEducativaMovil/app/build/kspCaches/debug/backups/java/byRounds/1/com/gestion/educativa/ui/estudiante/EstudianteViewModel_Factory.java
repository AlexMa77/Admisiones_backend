package com.gestion.educativa.ui.estudiante;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.EstudianteRepository;
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
public final class EstudianteViewModel_Factory implements Factory<EstudianteViewModel> {
  private final Provider<EstudianteRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public EstudianteViewModel_Factory(Provider<EstudianteRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public EstudianteViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static EstudianteViewModel_Factory create(
      Provider<EstudianteRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new EstudianteViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static EstudianteViewModel newInstance(EstudianteRepository repository,
      TokenManager tokenManager) {
    return new EstudianteViewModel(repository, tokenManager);
  }
}
