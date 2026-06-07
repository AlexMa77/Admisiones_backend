package com.gestion.educativa.ui.home;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.EstudianteRepository;
import com.gestion.educativa.data.repository.MatriculaRepository;
import com.gestion.educativa.data.repository.NotaRepository;
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
public final class StudentPortalViewModel_Factory implements Factory<StudentPortalViewModel> {
  private final Provider<EstudianteRepository> estudianteRepositoryProvider;

  private final Provider<MatriculaRepository> matriculaRepositoryProvider;

  private final Provider<NotaRepository> notaRepositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public StudentPortalViewModel_Factory(Provider<EstudianteRepository> estudianteRepositoryProvider,
      Provider<MatriculaRepository> matriculaRepositoryProvider,
      Provider<NotaRepository> notaRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.estudianteRepositoryProvider = estudianteRepositoryProvider;
    this.matriculaRepositoryProvider = matriculaRepositoryProvider;
    this.notaRepositoryProvider = notaRepositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public StudentPortalViewModel get() {
    return newInstance(estudianteRepositoryProvider.get(), matriculaRepositoryProvider.get(), notaRepositoryProvider.get(), tokenManagerProvider.get());
  }

  public static StudentPortalViewModel_Factory create(
      Provider<EstudianteRepository> estudianteRepositoryProvider,
      Provider<MatriculaRepository> matriculaRepositoryProvider,
      Provider<NotaRepository> notaRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new StudentPortalViewModel_Factory(estudianteRepositoryProvider, matriculaRepositoryProvider, notaRepositoryProvider, tokenManagerProvider);
  }

  public static StudentPortalViewModel newInstance(EstudianteRepository estudianteRepository,
      MatriculaRepository matriculaRepository, NotaRepository notaRepository,
      TokenManager tokenManager) {
    return new StudentPortalViewModel(estudianteRepository, matriculaRepository, notaRepository, tokenManager);
  }
}
