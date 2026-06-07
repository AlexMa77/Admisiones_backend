package com.gestion.educativa.ui.facultad;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.FacultadRepository;
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
public final class FacultadViewModel_Factory implements Factory<FacultadViewModel> {
  private final Provider<FacultadRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public FacultadViewModel_Factory(Provider<FacultadRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public FacultadViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static FacultadViewModel_Factory create(Provider<FacultadRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new FacultadViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static FacultadViewModel newInstance(FacultadRepository repository,
      TokenManager tokenManager) {
    return new FacultadViewModel(repository, tokenManager);
  }
}
